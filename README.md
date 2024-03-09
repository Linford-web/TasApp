# TasApp

 TASK AND APPOINTMENT MANAGEMENT ANDROID APPLICATION IN JAVA Using ANDROID STUDIO
 1. REGISTRATION and LOGIN
The application has a login and register activity where a user either registers as a Student or Teacher
The User registers as a Student or as a Teacher and gets directed to the login page to login, after logging in the user is directed to the appropriate Dashboard activity, either the Student Dashboard or Teacher Dashboard.

 2. TASK MANAGEMENT
The Application provides an activity to manage tasks.
Teachers can create Tasks and can delete them.
Created tasks can be marked as done by students and displayed in the DoneTask Activtiy.
Tasks Marked as done are displayed in the Done Activity. The user who created the Task and the user who marked the Task as Done.
 3. APPOINTMENT MANAGEMENT
Teachers can create Appointments, setting the time they are available for the appointment and set the TimeSpan in which they will be available.
Students can click on the Book button and are directed to a timeSpan Grid that has divided the Appointment with timeSpans of 15 minutes.
User can Click the available time and data is stored in a Firebase collection and displayed in the booked Appointment activity which will on show the Student and Teacher who have interacted with the appointment item.
 4. DATABASE
The Application is connected to a Firebase FireStore Database. 
