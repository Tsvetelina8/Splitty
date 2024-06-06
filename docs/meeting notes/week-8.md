| Key          | Value                                                                                                                                        |
| ------------ |----------------------------------------------------------------------------------------------------------------------------------------------|
| Date:        | 02-04-2024                                                                                                                                   |
| Time:        | 15:45 – 16:30                                                                                                                                |
| Location:    | Flux Hall B                                                                                                                                  |
| Chair        | Pieter van den Haspel                                                                                                                        |
| Minute Taker | Maksim Plotnikov                                                                                                                             |
| Attendees:   | Nedas Bolevičius, Tsvetelina  Ilieva, Kris Jankowska, Maksim Plotnikov, Pieter van den Haspel, Edwin van der Heijden and Sagar Chethan Kumar |

# Opening (`1` min)
*Here you check that everybody is present.*
Everybody was present on time

# Check-in (`1` min)
*How is everyone doing?*
Everybody was doing good. This week we finally managed to link database and server to client, which is a great milestone for our team.

# Announcements by the team (`2` min)
*Has anybody got some important things to tell?*
No announcements were made

# Approval of the last minutes (`1` min)
Everybody approved the minutes.

# Approval of the agenda (`1` min)
*Make sure everything that needs to be discussed is in the agenda or add it if something is missing.*
Everyone approves, nothing to add.

# Announcements by the TA (`2` min)
- Good news, everyone passed this week
- This Friday there is a deadline: self-reflection (individual)
- We can have one rubric reassessed **until the end of the week** (+ another assessment)
	- Probably HCI?
	- Contact Sagar
- Next week there will be no formal meeting
	- No feedback, no assessment
	- Still supposed to have the chair and the minute taker

# Presentation of the current app to TA (`5` min)
Feedback from TA:
- Any external links (in our case - Google Spreadsheet) should be mentioned on GitLab
- Syncing changes between clients automatically is a **basic requirement**
	- websockets or long-polling

# Talking Points: (Inform/ brainstorm/ decision making/ discuss) (`33` min)

- Last week work (`5` min)
    - What has everyone done in the past week related to the app?
	    - **Tsvety**: added scene to add new participant to the event + testing
	    - **Nedas**: overhauled database, touched frontend to link up participants + tests + removed redundant things
	    - **Edwin**: fixed admin screen issues from last time (filechooser, multiple events selection etc...)
		- **Maksim**: language contribution UI user access + tests.
		- **Kris**: wrote tests for client.
		- **Pieter**: connected the database to expenses in UI + tests.


- How far are we with the basic requirements? (`4` min)
	- pretty far
	- still have to make clients sync automatically (probably websockets & STOMP)
		- **TA**: Sebastian has an video example of longpolling out there
	- update and polish the UI
		- issues already in Gitlab, last chance for backenders to contribute to frontend
- Last week: expense and debts have to be connected and edit event screen has to be connected
	- done now


- How are we doing on the extra features (`5` min)
  - Language feature (Maksim)
	  - basically done
	  - enhancements / issues:
		  - adopt file chooser to add new translation
		  - empty the template on exit of contribution UI (changes should not persist)
  - Email notification (Nedas)
	  - this week?
  - Statistics (Tsvetelina)
	  - this week

>**TA note:** 
>- look into formative assignments
>	- try to minmax the rubrics

- Product pitch (`5` min)
	- underwhelming, but reflects the effort
- Did anyone struggle with writing script?
	- not really
	- people were late to start though
- What will everyone talk about during presentation?
	- **Idea**: everyone takes up on one basic feature, one extra feature
    - Basic features
	    - Nedas: database linkup
	    - Edwin: admin screen
	    - Kris: start screen
	    - Maksim: edit event screen
    - Extra features
	    - Nedas: emails
	    - Maksim: languages
	    - ...
- Do we do a practice session next week or the Monday before the presentation?
	- Possibly next week or the Monday before the presentation
	- Informal
		- just have a draft of what to say
		- compare
		- presentation: 8-10 minutes
		- we can get asked about something we're weak at

>**Note from Edwin:**
>- Gitlab issue templates now present
>- Try to reformat the newer issues using the template provided

- Rubrics (`10` min)
- HCI
    - We are missing a Help/About page that lists keyboard shortcuts
	    - Can be easily done using `Alert` / `DialogPane`
	    - Easy frontend contribution for our backenders
    - Missing undo operations
	    - User should be able to undo all change operations
	    - Requires effort, probably sacrificing that feature
    - Confirmation for admin deleting event (Edwin)
	    - All done except the pop up during event deletion
- Code contribution
    - Commit messages
	    - Make sure to make clear commit messages
- Tasks and planning
    - Issues have to be 3-4 hour sizes
	    - Group similar smaller issues to achieve that
    - Document rotation of roles in project (UI, backend, api, etc.)
	    - Should be done retrospectively
- Technology
    - @Controller in isolation using websockets
    - Update UI (images and non-default layout)
    - Add websockets and long-polling
- Testing
	- **TA:** As long as uses DI, we're good

- Any other points/issues to bring up (`4` min)
	- no issues were brought up


# Summarize action points: Who, what , when? (`3 min`)
- Everyone focuses on the rubrics discussed
- Next week we have an informal meeting to wrap things up
- Work on presentation for product pitch

# Feedback round: (`2` min)
*What went well and what can be improved next time?*
All meeting points were discussed

# Planned meeting duration != actual duration? (`1` min)
*Where/why did you mis-estimate the time?*
The duration of meeting was as planned.

# Question round (`2` min)
*Does anyone have anything to add before the meeting closes?*
- Are there any tasks left for backend (for frontenders to contribute to)
	- Event update request is still missing
	- websockets functionality
# Closing (`0` min)
*Now you can start working on the project. Good luck!*

╰( ͡° ͜ʖ ͡° )つ──☆*:・ﾟ