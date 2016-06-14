# GotchaMusicPlayer
## Problem:
SMSs were a lot popular in those days, when java based handsets are ruling the mobile world and android has just given a peekaboo with its stable HoneyComb. Those were days when none has heard about TRAI and SMS restrictions and regulations. Developers were happy when Google owned android and made it available for open source developers. Technocrats like me were looking for the wonders that they can do with android, I wanted to add fun to those list. As a third android project I wanted to try something different with Plain text SMS and audio player in android. An android application that plays music when you receive SMS from another mobile supporting Android 2.3. I created this app which plays music on receiving SMS from another mobile phone. Install in a friend's phone, send a plain old SMS and prank him by playing a song without his interference. Just see his scared face and say Gotcha! Believe me, got myself scared while testing random songs :P

## Approach:
SMS was a fancy mode of communication those days. It has unlimited powers(no SMS limits :P) than any other means of communication. Android was budding to be the platform for smart devices. The Approach is simple and straight forward. Adding a SMS receiving listener by granting, the application, to listen to the SMS inbox. At first the user should set the music directory on which the application should look for songs, on request, along with the check whether to send reply SMS to the requester after processing the request. On processing the text and finding the appropriate keywords or format that is readable by the application, the listener delegates the job to the song picker. The song picker tries to determine that the user has requested a specific song or any random song. In case of user given a song string, the song picker tries to pick the song from the file system based on the music folder path the user has set at the beginning. The song picker can recursively try to reach a sub directory level of 4 till it finds the given pattern in song name. If there is no matching song then the application replies that no matching song found to the requester, if configured initially. On the other hand, if the user requests a random song, then the system tries to reach any random level (< 5), on the configured directory and picks a random song. Once the song picker choses a song, the song player is started with the particular song automatically. After panicking(:-P), the phone user can just pick the phone, go to menus and stop the song player.
## Sequence:
![Image](https://file.ac/NlgIS0EBO0w/GotchMusicPlayerFlow.png)

## Commands and details:

- ``Gotcha play Random``- Plays random songs from the configured file path root.
- ``Gotcha play file`` **complete-file-path** - plays the song in the given path if exists.
- ``Gotcha play pattern`` **string-pattern** - Searches the given path hierarchy and plays any first song that matches the given string pattern.

## Challenges:
Well, I had no idea what android os is, and it looked like the file system access and playing songs were both hard. First I took a deep breath. Nothing is impossible, if we try. I had gone through all the tutorials that were coming on youtube related to this. Finally used the tree recursion to simplify the file choosing operation. Reading SMS were easy as a it is based on my previous java,J2ME mobile applications on same path. Being naive, I started with all code in the same class file, but later I refactored them to appropriate packages.
## Views:
### HomeActivity:
![Image](https://file.ac/NlgIS0EBO0w/image1.png)

## References:

- Sending and Receive sms in Android 2.3: http://www.vineetdhanawat.com/blog/2012/04/how-to-use-broadcast-receiver-in-android-send-and-receive-sms/
- Playing music: http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
- http://stackoverflow.com/a/16128778
- Sending SMS from application: http://www.mkyong.com/android/how-to-send-sms-message-in-android/
- SQLite read and write using SQLiteOpenHelper: http://android-er.blogspot.in/2011/06/simple-example-using-androids-sqlite_02.html
