| Key | Value |
| --- | --- |
| Date: | 27-02-2024  |
| Time: | 15:45 – 16:30  |
| Location: | Flux Hall B |
| Chair | Tsvetelina Ilieva |
| Minute Taker | Nedas Bolevičius |
| Attendees: | Nedas Bolevičius, Tsvetelina  Ilieva, Kris Jankowska, Maksim Plotnikov, Pieter van den Haspel, Edwin van der Heijden and Sagar Chethan Kumar |

# Opening (`1` min)
*Here you check that everybody is present.*

# Check-in (`1` min)
*How is everyone doing?*   

Everyone was doing fine and happy :)

# Announcements by the team (`2` min)
*Has anybody got some important things to tell?*

# Approval of minutes
We should merge minutes into the agenda of the previous week, don't put them into a doc file (use markdown instead).

# Approval of the agenda (`2` min)
*Make sure everything that needs to be discussed is in the agenda or add it if something is missing.*

Agenda sent a little late, however no changes need to be made explicitly.

# Announcements by the TA (`2` min)
For this week, we have a code of conduct deadline.
We all passed the git assignment.

Make sure we have our commits and requirements done every week, this is mandatory (but repairable).
Will get more info about required contributions later this week.

# Presentation of the current app to TA (`5` min)
Presented the start screen UI, where we can create new events, join event and delete events (mock implementations). 
There are more UI scenes present, however no clear ways to enter them yet.

Made common entity classes with boiler-plate implementations, which are mostly fully tested. 
We might need to slightly refactor/streamline our testing classes, fix naming schemes.

# Talking Points: (Inform/ brainstorm/ decision making/ discuss) (`40` min)

 - Last week work (`5` min)
     - What has everyone done in the past week related to the app?    

        **Nedas**: Fixed most of the checkstyle issues, added a suppression filter for the javadoc rule in test files. Added unit tests for common's classes and started work on Event controller.    
        **Edwin**: Looked at which entities are needed, created a small schema. Made common Event entity. Worked on code of conduct. Created issues.    
        **Maksim**: Made JavaFX scenes and controllers for them. Mostly worked on visual client side things. Tried to fix client-side checkstyle issues.    
        **Kris**: Made the entire start screen and the mock functionality for it, which was presented to the TA.    
        **Pieter**: Finished Code of conduct. Made expense entity, loan entity. Made the decision himself how to design the expense class, which we may need to discuss. Made some tests for Loan and Expense entity classes.    
        **Tsvetelina**: Made tag class and some changes to common's classes. Created tests for more common's entities classes.    
 - Code of conduct (`5` min)
     - How far is everyone with their parts?    

        Everyone is done with their parts, however we have not yet reached the required word count. 
     - Any questions/problems?    
     
        We should all collectively expand the Code of Conduct slightly to reach the necessary word count.
     - Putting it in a latex file    
     
        Tsvetelina volunteers to put it all together into a single latex file once the Code of Conduct is finalized.
 - Extentions (`10` min)
     - Decide on which extentions we are going to implement    

        We are currently implementing code which could be attributed to the extensions, so we need to discuss whether we are actually developing them or if we should get rid of those changes/not work on them.
        After the meeting, look through the extensions which might require pre-emptive planning and develop code having these in mind. This is done so we don't have more difficulties afterwards IF we decide to implement these.
 - Checkstyle rules (`10` min)
     - Should we use public or private attributes?    

        We decided to keep common's entities public, and all others private.
     - Line length    

        We will keep the Line length rule at the default values
     - Number of parameters in a method    

        We will keep the Number of parameters rule at the default values.
     - Javadocs for method length    

        Keep Javadocs rule, even for methods which might only span 1 line and may not necessarily require comments, for the sake of consistency.
     - Testing folder's checkstyle rules    

        Currently keeping only Javadoc suppression for testing files, however if any issues eventually arise, we should discuss this decision.

     - Naming consistency and type consistency.     
        In some places we use String to store Dates, in others we use a separate object. We should standardize these issues. Since these issues are already in main, we should be more careful with reviewing Merge Requests.
 - Gitlab (`5` min)
     - Use of Gitlab issues    

        Look into templates for issues for standardized issue-making.     
        We have made labels, milestones and other categorizations for GitLab issues.    
        Make sure to assign yourself the issue that you are currently working towards.    
        Look into how to use Milestones/Epics more properly, potentially use sprints which run every week.    
     - Use of Gitlab merge requests    

        Not commit to other people's branches to avoid conflicts, try approve/merge quicker and make sure our branches are as up to date with main as possible.    
 - Tasks & Planning (`5` min)
 
 - Other points we discussed during the meeting    

    Make a conceptual schema for easier implementation of entities, controllers and general flow of data.    
    Remove unnecessary classes which are left over from the template project.

# Summarize action points: Who , what , when? (`5 min`)
 - Any deadlines for next week?    

    Finish up the code of conduct, make sure we have over 2500 words as soon as possible.     
 Make a conceptual schema so the team is on the same page with implementation details of everything related to data.
 - Fill the scrum board.
 - Any research topics    

    Look into a more proper way to use GitLab issues.    
 Look into extensions and whether we want to develop code with certain extensions in mind.

# Feedback round: (`4` min)
*What went well and what can be improved next time?*    
Generally, the talking points were a lot more clear, we were able to go into a lot more detail with specific issues. The meeting was a lot more productive and more members were able to speak/participate. We still need to encourage all members to speak equally.

# Planned meeting duration != actual duration? (`3` min)
*Where/why did you mis-estimate the time?*    
We ended the meeting a few minutes early (a.k.a on time), however the planned times for individual bullet points were often not accurate.

# Question round (`2` min)
*Does anyone have anything to add before the meeting closes?*

# Closing (`1` min)
*Now you can start working on the project. Good luck!*
