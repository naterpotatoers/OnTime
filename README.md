# OnTime - Android Application
**Overview**\
An appointment scheduling app that makes booking appointments easier for Clients. This app is expected to give Businesses a platform to set an availability, then Clients to easily book appointments, and in return give providers a simple way to view Client information of appointments booked. 

**Two Types of Users: Businesses & Clients**\
This app has two types of users, a Business and a Client. 
The Business is able to register and sign up, then be able to login. Once they have logged in they will be brought to the Business Home Screen; this is where they can view booked appointments, set availability or log out.\
Clients will be able to search for the provider by name, select a date, then get a list of available appointments on that day to choose from. After choosing a time the Client will enter in contact information that will only be sent to that Business. If the appointment is still available, the final screen for the Client is the Confirmation Page.

**Build Status: Complete, but with certian Expectations from User or Functionality**
1. When booking appointments we expect Client to be honest and not a spammer.
2. We expect the Client to screenshot the appointment confirmation screen for reference or write that information down, because once that screen is gone there is no way to get that information without contacting the Business. 
3. Business must contact the client in order to confirm appointments. 
4. Business is unable to delete appointments 
5. When setting availability the appointments can only be set to 1 hour increments

**Running the App**\
This app is currently running with Android Studio. The Emulator of choice is a Pixel device. Any Android Emulator will do, however if you don't use a Pixel device you may run into graphical UI issues. Those issues are limited to spacing not aligned, text overlapping, and search bar or drop down options not outputted as expected. No evidence of functionality being compromised. 

**Using the App**\
After launching the app you will be brought to our home page. If you ever want to return to the home page, just press the "OnTime" graphic at the top (may not seem like it at first, but it is a button). 

**Verifying the Database Works**\
In lieu of giving you access to our Firebase Console I will rely on the next section, "Testing the App", to suffice as verification the information is being saved to the database. This app will save and output the following types of information: the Business name after registering, times for availability and Client information. 
I hope to make testing basic functionality easier for you as a grader. 

**Testing the App**\
The flow created for testing the app is as follows, but feel free to do what makes sense:
1. Register a Business
2. Login
3. Set Availability 
4. Logout
5. Book Appointment as Client for Business we just created 
6. Repeat for Different Days and Times
7. Log back into Business to view booked appointments

## Video Walkthrough

Here's a walkthrough of Book Appointment feature:

<img src='https://i.gyazo.com/959a9dc2a4fc061781e2e999eaf464b4.gif' width='' alt='Video Walkthrough' />

