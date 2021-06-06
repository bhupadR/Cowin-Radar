# Cowin-Radar

Use this project to book vaccination slot in India. The cowin api used in this project are private API's which need an otp. One OTP can make a maximum of 21 api calls. Everytime you will have to provide OTP manually after certain interval of time(like copy and paste). 

Run this spring boot project from an IDE like Intellij. 

So once this project is started, a scheduler AppointmentFinderV2Scheduler will run at some interval configured by cron expression. The scheduler will generate otp and expect an otp in the console. Provide the OTP in console within 3 minutes of generation as OTP expires. Once OTP is validate and auth token is generated, schedular will look for available vaccination slot in a district for 7 days.

After some time period the authentication token will expire and the schedular will trigger new otp. Schedular will again wait for an otp. If a center is available Schedular can also book the center. No need to book the center manually.

To run this project some properties are necessary and need to be placed are specific path /opt/conf/cowin-radar/outsource/application.properties

Add this below properties in the application.properties file


appointment.private.scheduler.cron=1 * * * * *

############# Cowin API Properties #################
cowin.api=https://cdn-api.co-vin.in/api/

############ Column Specification #############
center.column.map={'name':'Center Name','pincode':'Pincode','fee_type':'Fee Type'}
session.column.map={'date':'Date','available_capacity':'Available Capacity','min_age_limit':'Age Limit','vaccine':'Vaccine Name'}

############## Telegram Bot token ######################
token=


############# Generate OPT secret ################
generate.otp.secret=U2FsdGVkX18jgVMLXdH8YxfSapanIAlcPscovzSfwLdfmKtkxw0EBaa9rn7uHga2m2yudKc65tatfkC0/nwXRQ==

server.port=0

################## Telegram number ##################
admin.number=

################## Cowin Registered number #################
admin.phone.number=

################# list of telegram number to trigger notification ###################
list.of.numbers=

################## list of beneficiary id to book slot #####################
beneficiary.id.list=

############# Search center in District ################
district_id=363



bhupad.rathi@gmail.com
