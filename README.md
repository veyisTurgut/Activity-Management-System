# Activity-Management-System

I implemented project using Java Spring for backend, ReactJs for frontend, Flutter for mobile and Postgres as DB. 

There are two types of users: end user and admin.
Admin has to login with his/her own username-password and his/her session will expire after a while.
Admin has CRUD operations on activites also s/he can list participants as barchart or list. 
Admin sets quotas on activites, when quota is filled end user cannot enroll an activity.
User lists activities s/he enrolled.
User lists active activities(startDate >= today) and enrolls if there is enough room.
User sees the location of the activity on maps and gets an email with QR Code which contains user-activity infos after enrollment.
User can enroll only once to an activity.  

Pop-up toast will be displayed telling an operation is whether successful or not.
