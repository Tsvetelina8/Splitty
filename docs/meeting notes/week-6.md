| Key          | Value                                                                                                                                        |
| ------------ |----------------------------------------------------------------------------------------------------------------------------------------------|
| Date:        | 19-03-2024                                                                                                                                   |
| Time:        | 15:45 – 16:30                                                                                                                                |
| Location:    | Flux Hall B                                                                                                                                  |
| Chair        | Nedas Bolevičius                                                                                                                             |
| Minute Taker | Edwin van der Heijden                                                                                                                        |
| Attendees:   | Nedas Bolevičius, Tsvetelina  Ilieva, Kris Jankowska, Maksim Plotnikov, Pieter van den Haspel, Edwin van der Heijden and Sagar Chethan Kumar |

# Opening (`1` min)
*Here you check that everybody is present.*

Everybody was here on time.

# Check-in (`1` min)
*How is everyone doing?*
Everybody was doing fine :)

# Announcements by the team (`2` min)
*Has anybody got some important things to tell?*

No announcements to be made.

# Approval of the last minutes (`1` min)
Everybody approved the minutes.

# Approval of the agenda (`1` min)
*Make sure everything that needs to be discussed is in the agenda or add it if something is missing.*

The agenda was approved, Edwin suggested to talk about the tasks and planning assignment review if we have time.

# Announcements by the TA (`2` min)
The HCI and Testing assignments are due this week. We should look at the rubrics on how to do these parts correctly.
For the TA's to test our project, we need to configure the README on how to run the project, and how to configure everything.
Besides that we all passed the knockout criteria for week 4/5.


# Presentation of the current app to TA (`5` min)
Changes that have been made:
 - Database controllers and client integration (by Nedas, Pieter)
 - Language contribution UI scenes (by Maksim)
 - Json importing and exporting (by Edwin)
 - Invite codes (by Nedas)
 - Client Tests (by Kris)
 - Admin scenes (by Edwin)
 - Expenses (by Tsvetelina)
 
We showed to our TA how we could create a new event, and put in the database. We also showed the admin scene, and the language contribution ui.

There were a few things not working as expected, such as importing an event giving an error because of the LocalDateTime conversion, and the main page on localhost giving no response yet. 
It would also be good to have the invite code to be copyable. This should be fixed by next meeting.

# Talking Points: (Inform/ brainstorm/ decision making/ discuss) (`30` min)
 - Last week work (`5` min)
     - What has everyone done in the past week related to the app?  
        **Nedas**: Worked on invite codes, and the event database controllers/api. He also fixed a few small bugs like with the post api not working because of jackson and an incorrectly named method.    
        **Edwin**: Mainly worked on the admin screen, creating it with via a password input (not functional yet), and importing/exporting an event to JSON. Database functionality still to be done. The was also a bit of work on small code cleaning/bugfixes and working on organizing on GitLab.  
        **Maksim**: Made the language contribution UI and screens. It is now possible to create and use your own language for your locale. Switching languages to be implemented yet.  
        **Kris**: Mostly did testing and bugfixes, relating to the start screen.      
        **Pieter**: Worked on database controllers for the loan and expenses entities, as well as testing them partially.  
        **Tsvetelina**: Made methods for expenses and debt calculating. All debts are now possible to be calculated, but it is still to be needed to have partial settling of debts and calculating debts per person.
 - Testing (`7` min)
   - Formative assessments for this week
   - Currently at 20% test coverage, most front-end MRs do not have tests
   - Multiple exceptions are printed to console, missing file errors clutter output.


   We mainly need to do javafx tests and frontend. We also need to test any code we commit with every merge request, because right now we aren't testing anything and bringing the testing
   percentage down.Not adding tests with new MRs. We should look through the rubrics to see what to do. We also need to fix the console output, right now it's messy with all kinds of 
   warnings and errors between messages.
 - HCI/Usability (`7` min)
    - Formative assessments for this week
    - Three categories graded: Accessibility, Navigation, Feedback.
    - Feedback (errors) are printed to console, discuss whether that needs to be reworked.


   Errors are printed to console right now, it should give feedback to the user instead, maybe with an error screen. We should also take a look at navigation parts, and a bit of visualization,
   like using icons and showing what keys to what actions. We should work on usability now, and try making it look nicer around the end of the project. Responsiveness should also come a bit later.
 - What to focus on (`7` min)
	 - Next week we have a formative assessment for implemented features, are we on track?
     - State of admin controls and Json? (Edwin)
     - State of language switch and UI? (Maksim)
     - State of expense calculations? (Tsvetelina + Pieter backend)

We are looking to have main functionality/user stories to be done around next week. The admin controls and the language switch are nearing the end and are almost done. The part where we are most
behind is the expense calculation. Debt settling isn't done yet, and the backend isn't connected yet. It is important to have this part done as soon as possible.
 - Any other points/issues to bring up (`4` min)
     - Checkstyle issues for Main control constructor (do we allow checkstyle exceptions?)
       - We aim to have no checkstyle exceptions, we either keep the way we have it now (splitting the constructor), or fix it with making an array.  This issue will be looked into this week.
     - Front-end tests requiring new interfaces/classes clutter
       - It might be a thing to look at better type of frontend testing, as creating a mock class like this could be too much of an issue with cluttering. An interface could be a solution.
     - Tasks and planning
       - We are planning, but only in meetings, it is not reflected well in GitLab, which will give issues for grading. Relect this more in using sprints or creating better
       issues/milestones. Effort spent also needs to be tracked by everybody! We should add more issues now, and do a bit of planning forward, as well as improving on past, already closed 
       issues. Look at the grading/rubrics for the assignment to see where we can improve.

# Summarize action points: Who , what , when? (`3 min`)
 - Any deadlines for next week?
 - Fill the scrum board.
 - Any research topics

We should finish up all the User Stories, by next week.
We should also do more testing, aim for around 50% tested.
Nedas will try looking into usability and navigation, somebody else should look into feedback with the console spamming.
For the frontend, everybody should try taking rubrics in account
We will do a bit of switching the roles, everybody try to focus on things they haven't done much of yet.
For the Tasks and planning: Fill in the time tracking, Edwin will fix the milestones, labels and issues, everybody should also make own issues and assign them to create a planning.

# Feedback round: (`2` min)
*What went well and what can be improved next time?*  
Everything was covered nicely, although there was not a lot of discussion compared to weeks before. Because of the break week, it was essentially more of a reminder meeting, to remind us about
what needs to be done.

# Planned meeting duration != actual duration? (`1` min)
*Where/why did you mis-estimate the time?*  
Everything took a bit less time than expected, but wee did flow easily from subject to subject.

# Question round (`2` min)
*Does anyone have anything to add before the meeting closes?  
It was asked why last week we got a very good for the chair, but there was no negative feedback. Therefore, it was changed to excellent.

# Closing (`0` min)
*Now you can start working on the project. Good luck!*  
Goodbye, have a nice day :)
