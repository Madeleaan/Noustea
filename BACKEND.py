import time
import network
import socket
from machine import Pin, PWM, I2C
import utime

# DEFINE ALL THE LEDS & MOTORS
module1_r = Pin(4, Pin.OUT)
module1_g = Pin(5, Pin.OUT)
module1_b = Pin(28, Pin.OUT)

module2_r = Pin(27, Pin.OUT)
module2_g = Pin(26, Pin.OUT)
module2_b = Pin(22, Pin.OUT)

module3_r = Pin(21, Pin.OUT)
module3_g = Pin(20, Pin.OUT)
module3_b = Pin(19, Pin.OUT)

module4_r = Pin(18, Pin.OUT)
module4_g = Pin(17, Pin.OUT)
module4_b = Pin(16, Pin.OUT)

motor_pin = Pin(3, Pin.OUT)
motor_pwm = PWM(motor_pin)

servo0_pin = Pin(0, Pin.OUT)
servo0_pwm = PWM(servo0_pin)
servo1_pin = Pin(1, Pin.OUT)
servo1_pwm = PWM(servo1_pin)
servo2_pin = Pin(2, Pin.OUT)
servo2_pwm = PWM(servo2_pin)

servo0_pwm.freq(50)
servo1_pwm.freq(50)
servo2_pwm.freq(50)

dev = I2C(1, scl=Pin(15), sda=Pin(14))

print(dev.scan())

address = 72

colors = ["000", "111", "100", "010", "001", "011", "101", "110"]

ssid = 'NousteaBackend'
password = 'HelloWorld'
###########################################################
#DEFINE I2C METHODS
def readConfig():
    dev.writeto(address, bytearray([1]))
    result = dev.readfrom(address, 2)
    
    return result[0]<<8 | result[1]

def readValueFrom(channel):
    config = readConfig()
    config &= ~(7<<12) # clear mux
    config &= ~(7<<9) # clear pga
    config |= (7 & (4 + channel))<<12
    config |= (1<<15) # trigger next conversion
    config |= (1<<9) # gain 4V

    config = [int(config>>i & 0xff) for i in [8,0]]
    dev.writeto(address, bytearray( [1] + config))

    config = readConfig()
    while(config & 0x8000) == 0:
        config = readConfig()
    dev.writeto(address, bytearray([0]))
    result = dev.readfrom(address, 2)

    return (((result[0]<<8 | result[1]) + 2)  & 0xffff)

###########################################################
#RESET ALL COMPONENTS
servo0_pwm.duty_u16(5000)
servo1_pwm.duty_u16(5000)
servo2_pwm.duty_u16(5000)
motor_pwm.duty_u16(0)
module1_r.value(1)
module1_g.value(1)
module1_b.value(1)
    
module2_r.value(1)
module2_g.value(1)
module2_b.value(1)
    
module3_r.value(1)
module3_g.value(1)
module3_b.value(1)
    
module4_r.value(1)
module4_g.value(1)
module4_b.value(1)
###########################################################

wlan = network.WLAN(network.STA_IF)
wlan.active(True)
wlan.connect(ssid, password)

html = ""

# Wait for connect or fail
max_wait = 20
while max_wait > 0:
    if wlan.status() < 0 or wlan.status() >= 3:
        break
    max_wait -= 1
    print('waiting for connection...')
    time.sleep(1)
    
# Handle connection error
print(wlan.status())
if wlan.status() != 3:
    raise RuntimeError('network connection failed')
else:
    Pin("LED", Pin.OUT).high()
    print('Connected')
    status = wlan.ifconfig()
    print( 'ip = ' + status[0] )
    
    
# Open socket
addr = socket.getaddrinfo('0.0.0.0', 80)[0][-1]
s = socket.socket()
s.bind(addr)
s.listen(1)
print('listening on', addr)

# Listen for connections, serve client
while True:
    try:       
        cl, addr = s.accept()
        print('client connected from', addr)
        request = cl.recv(1024)
        request = str(request)
        if request.find('GET /favicon.ico') == -1:
            print(request)
        
        set_servo = request.find('set_servo=')
        set_module_col = request.find('set_module_col=')
        set_motor = request.find('set_motor=')
        
        if set_servo == 8:
            servoId = request[18:request.find(' HTTP')].split("|")[0]
            if servoId == '0':
                servo0_pwm.duty_u16(int(request[18:request.find(' HTTP')].split("|")[1]))
            elif servoId == '1':
                servo1_pwm.duty_u16(int(request[18:request.find(' HTTP')].split("|")[1]))
            elif servoId == '2':
                servo2_pwm.duty_u16(int(request[18:request.find(' HTTP')].split("|")[1]))
            else:
                print("Invalid servo id!!!!")
            
            print("Changing servo...")
        if set_module_col == 8:
            module_pos = int(request[23:request.find(' HTTP')].split("|")[0])
            color_code = colors[int(request[18:request.find(' HTTP')].split("|")[1])]
            
            if module_pos == 1:
                module1_r.value(int(color_code[0]))
                module1_g.value(int(color_code[1]))
                module1_b.value(int(color_code[2]))
            elif module_pos == 2:
                module2_r.value(int(color_code[0]))
                module2_g.value(int(color_code[1]))
                module2_b.value(int(color_code[2]))
            elif module_pos == 3:
                module3_r.value(int(color_code[0]))
                module3_g.value(int(color_code[1]))
                module3_b.value(int(color_code[2]))
            elif module_pos == 4:
                module4_r.value(int(color_code[0]))
                module4_g.value(int(color_code[1]))
                module4_b.value(int(color_code[2]))
            else:
                print("Invalid module id!!!! " + module_pos) 
        if set_motor == 8:
            motor_speed = int(request[18:request.find(' HTTP')])
            if motor_speed != 0:
                motor_pwm.duty_u16(60_000)
                utime.sleep(0.1)
            motor_pwm.duty_u16(motor_speed)
        # Create and send response
        cl.send('HTTP/1.0 200 OK\r\nContent-type: text/html\r\n\r\n')
        cl.send(html)
        cl.close()
        
    except OSError as e:
        cl.close()
        print('connection closed')