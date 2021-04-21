# Kitaab

[![License](https://img.shields.io/badge/License-MIT-green)](https://github.com/NikDev9/Kitaab/blob/main/LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.72-blue)](https://kotlinlang.org/docs/releases.html)

<img align="left" width="80" height="80" src="https://github.com/NikDev9/Kitaab/blob/main/app/src/main/res/mipmap-hdpi/ic_launcher_round.png">

It is a Kotlin based android application made for book readers. It is named Kitaab based on the Hindi word meaning a book. The app has various books from different genres, including short stories (novellas). Readers can also publish their own books on the app. A unique feature of this application is that it has a community section where readers can have conversations about books. There is a channel dedicated to each genre where people can discuss about their likings and give/take suggestions. 

<p align="center">
  <img src="https://media.giphy.com/media/kfYBnso8PSH8ZYTDG4/giphy.gif"> 
</p>

## Table of contents
  
- [Installation](#installation)  
- [Requirements](#requirements)  
- [Architecture](#architecture)    
- [Basic usage](#basic-usage)  
- [Features](#features)  
- [Planned features](#planned-features)
- [Credit](#credit)
- [License](#license)
- [Maintenance](#maintenance)

## Installation  

To use this code, follow the steps given below:
- Clone this repository.
- Make a new project from version control on android studio. (File -> New -> Project from version control)
- In the 'Get from version control' screen, Paste the HTTPS URL in the URL field.
- Click on clone et voila, you are ready to go.

### External Dependencies

- Firebase dependencies:  
  Firebase database to store data of users, community and books.  
  Firebase storage to store files like pdfs, images.  
  Firebase auth to login and register users on the application.  
 Other steps required to set up firebase for your project are given here: [Firebase setup](https://firebase.google.com/docs/android/setup#analytics-not-enabled)  
  
NOTE: '-ktx' has to be added for Kotlin based projects.
 
 ```
 implementation platform('com.google.firebase:firebase-bom:26.7.0')
 implementation 'com.google.firebase:firebase-auth-ktx'
 implementation 'com.google.firebase:firebase-database-ktx'
 implementation 'com.google.firebase:firebase-storage-ktx'
 ```
 
 - To load images from download URL and filepath retrieved from firebase storage
 ```
 implementation 'com.squareup.picasso:picasso:2.71828'
 ```
 
 - To open pdfs on the application
 ```
 implementation 'com.github.barteksc:android-pdf-viewer:2.8.2'
 ```
 
 ### Database GIST
 
 A snippet of data from the firebase database in json: [Firebase database GIST](https://gist.github.com/NikDev9/3bbcfa1c52d95404d77dba0d581fe284)

## Requirements  

- Firebase Database
- Android studio
- Supports android API level 22 and above

## Architecture  

The project has been developed using MVVM (Model-View-ViewModel) architecture. There are 5 packages in the project which contain Kotlin class files separated according to their functionalities. Those packages are:
- Adapter: It contains all the adapters that render recyclerview items in the layout.
- Fragments: It contains fragments that can be added to the view dynamically.
- Model: All the data classes reside in this package.
- View: All the activities are inside this package. They handle all layout related operations and get data from their respective ViewModels via data classes. 
- ViewModel: It has Kotlin class files handling business logic, which are also responsible for retrieving, storing and updating data on firebase.

## Basic usage  

To use the app, follow these steps:
- Install the app from the apk provided [here](https://github.com/NikDev9/Kitaab/releases/tag/v1.0-beta).
- Signup via the register page on the app.
- Enjoy all the features of the app.

## Features

- Secure user login/register: Users have to signup on the app to use all its features. It is secure as firebase auth handles login and signup. Hence, account passwords are not visible to anyone.
- Browse library: The library has short stories (novellas) and books from various genres.
- View book details, user reviews and ratings of any book: Users can add review and rating of a book if it has been added to their favourites list.
- Add book to favourites: By adding a book to favourites list, one can read that book by going to 'your library' section in the app.
- Remove book from favourites: One can remove a book from favourites any time.
- Read books: Users can open a book by clicking on the book they want to read in their own library. Book pages are changed by swiping horizontally. Pages can also be zoomed in and out by pinching. When a user closes the book just by going back, the last visited page is saved on the database. So, the user can continue reading from wherever he/she left off next time.
- Publish a book: Users can publish their own books in pdf format. All the published books can be seen in the user's library.
- Delete published book: Users can delete their published books completely from the database.
- Community section: It has channels for each genre. Users can join any community and chat with fellow readers about books and fun stuff.


<p align="center">
  <img width="180" height="320" src="https://user-images.githubusercontent.com/46367876/115452137-adf1b080-a23b-11eb-8b63-488b83a9bf48.png">
  <img width="180" height="320" src="https://user-images.githubusercontent.com/46367876/115443503-fbb4eb80-a230-11eb-99ec-f05a9603259c.png">
  <img width="180" height="320" src="https://user-images.githubusercontent.com/46367876/115452960-b4ccf300-a23c-11eb-9d58-71bd04887b2e.png">
  <img width="180" height="320" src="https://user-images.githubusercontent.com/46367876/115443523-00799f80-a231-11eb-9eff-0be31efbceeb.png">
  <img width="180" height="320" src="https://user-images.githubusercontent.com/46367876/115443446-e770ee80-a230-11eb-9910-97dedd7464bd.png">
  <img width="180" height="320" src="https://user-images.githubusercontent.com/46367876/115624838-cedc0380-a318-11eb-9234-c36abd022c1a.png">
</p>

## Planned features

- Filter to search by author's or book's name.
- View other user's profiles.
- Add another user role (admin) who can approve a user's book before it is published. 

## Credit

All the books on the app are from [obooko.com](https://www.obooko.com/)

## License

Click on this badge to view the license. [![License](https://img.shields.io/badge/License-MIT-green)](https://github.com/NikDev9/Kitaab/blob/main/LICENSE)

## Maintenance

This repository is maintained by Nikita Desai.  
Feel free to create an issue if you need any help or want to give a suggestion.
