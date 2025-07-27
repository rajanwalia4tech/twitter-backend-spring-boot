# twitter-backend-spring-boot
The Twitter Backend Project is an API built with Java Spring Boot and MySQL, designed to support key functionalities of a social media platform.

# 🐦 Twitter Backend API Documentation

## 📌 Overview

The Twitter Backend Project is an API built with Node.js and MySQL, designed to support key functionalities of a social media platform.

## ✅ Prerequisites

*    Java, SpringBoot
*    Git
*    MySQL
*    DB Visualization tools (Sequel Ace, MySQL Workbench, phpMyAdmin)
*    Postman

* * *

## 📄 Swagger API Document URL
```
http://localhost:{port}/swagger-ui/index.html
```

## 🧩 Functional Requirements

### 1\. User Authentication and Authorization

*    **User Registration**: `POST /users`
*    **User Login**: `POST /users/login`
*    **JWT Token-Based Security**
*    **Password Recovery (optional)**

### 2\. User Profile Management

*    **View Profile**: `GET /users/profile`   
*    **Edit Profile**: `PATCH /users` 
*    **Follow/Unfollow**: `PUT` / `DELETE /users/{userid}/follow`
*    **View Followers / Following**: `GET /users/{userid}/followers`, `GET /users/{userid}/followees`   
*    **Update Profile Picture**: `PATCH /users/profile-picture` 

### 3\. Tweet Management

*    **Create Tweet**: `POST /tweets`
*    **Edit Tweet**: `PATCH /tweets`
*    **Delete Tweet**: `DELETE /tweets/{tweetId}`  
*    **Get Tweets**: `GET /tweets/{userId}`   
*    **Like / Unlike Tweet**: `PUT` / `DELETE /tweets/{tweetId}/like`   
*    **Comment**: `PUT /tweets/{tweetId}/comment`   
*    **Delete Comment**: `DELETE /tweets/{tweetId}/comment/{commentId}`   

### 4\. Search Functionality

*    **Search Tweets by Content/Hashtags** (TBD)
*    **Search Users by Username** (TBD) 

* * *

## 🗄️ Database Setup

### Create Database and User

```sql
CREATE DATABASE twitter_backend_db;
CREATE USER 'twitter_backend_user'@'localhost' IDENTIFIED BY 'Twitter_Backend_Password@123';
GRANT ALL PRIVILEGES ON twitter_backend_db.* TO 'twitter_backend_user'@'localhost';
```

### Tables

#### `users`

```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  bio TEXT DEFAULT NULL,
  gender ENUM("MALE", "FEMALE", "OTHERS") NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  profilePicture VARCHAR(255) DEFAULT NULL,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### `follows`

```sql
CREATE TABLE follows (
  id INT AUTO_INCREMENT PRIMARY KEY,
  followerId INT NOT NULL,
  followingId INT NOT NULL,
  active BOOLEAN DEFAULT 1,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (followerId) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (followingId) REFERENCES users(id) ON DELETE CASCADE
);
```

#### `tweets`

```sql
CREATE TABLE tweets (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userId INT NOT NULL,
  content VARCHAR(280) NOT NULL,
  isDeleted BOOLEAN DEFAULT 0,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);
```

#### `likes`

```sql
CREATE TABLE likes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userId INT NOT NULL,
  tweetId INT NOT NULL,
  isDeleted BOOLEAN DEFAULT 0,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (tweetId) REFERENCES tweets(id) ON DELETE CASCADE
);
```

#### `comments`

```sql
CREATE TABLE comments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userId INT NOT NULL,
  tweetId INT NOT NULL,
  content TEXT NOT NULL,
  isDeleted BOOLEAN DEFAULT 0,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (tweetId) REFERENCES tweets(id) ON DELETE CASCADE
);
```

#### `hashtags`
```sql
CREATE TABLE hashtags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tag VARCHAR(100) UNIQUE NOT NULL
);
```

#### `tweet_hashtags`

```sql
CREATE TABLE tweet_hashtags (
  tweet_id INT NOT NULL,
  hashtag_id INT NOT NULL,
  PRIMARY KEY (tweet_id, hashtag_id),
  FOREIGN KEY (tweet_id) REFERENCES tweets(id) ON DELETE CASCADE,
  FOREIGN KEY (hashtag_id) REFERENCES hashtags(id) ON DELETE CASCADE
);
```

#### Indexes

```sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_tweets_content ON tweets(content);
```

* * *

## 🔌 API Endpoints

### Users

#### `POST /users` – Register a user

**Request**:

```json
{
  "name": "John",
  "email": "john@example.com",
  "password": "12345678",
  "gender": "MALE"
}
```

**Response**:

```json
{
  "message": "User created successfully",
  "userId": 1
}
```

#### `POST /users/login` – Login

**Request**:

```json
{
  "email": "john@example.com",
  "password": "12345678"
}
```

**Response**:

```json
{
  "token": "JWT_TOKEN",
  "userId": 1
}
```

#### `GET /users/profile` – Fetch profile

**Response**:

```json
{
  "id": 1,
  "name": "John",
  "email": "john@example.com",
  "bio": null,
  "gender": "MALE",
  "profilePicture": null
}
```

#### `PATCH /users` – Update profile

**Request**:

```json
{
  "name": "Johnny",
  "bio": "I like tech."
}
```

**Response**:

```json
{
  "message": "Profile updated"
}
```

#### `PATCH /users/profile-picture`

**Request**:

```json
{
  "profilePicture": "https://example.com/pic.jpg"
}
```

**Response**:

```json
{
  "message": "Profile picture updated"
}
```

#### `PUT /users/{userId}/follow` / `DELETE`

**Response**:

```json
{
  "message": "Followed/Unfollowed successfully"
}
```

#### `GET /users/{userId}/followers`

**Response**:

```json
[
  {
    "id": 2,
    "name": "Alice"
  }
]
```

#### `GET /users/{userId}/followees`

**Response**:

```json
[
  {
    "id": 3,
    "name": "Bob"
  }
]
```

* * *

### Tweets

#### `POST /tweets`

**Request**:

```json
{
  "content": "My first tweet!"
}
```

**Response**:

```json
{
  "message": "Tweet posted",
  "tweetId": 1
}
```

#### `GET /tweets/{userId}`

**Response**:

```json
[
  {
    "id": 1,
    "content": "My first tweet!",
    "createdAt": "2025-06-29T08:00:00Z"
  }
]
```

#### `PATCH /tweets`

**Request**:

```json
{
  "tweetId": 1,
  "newContent": "Updated tweet!"
}
```

**Response**:

```json
{
  "message": "Tweet updated"
}
```

#### `DELETE /tweets/{tweetId}`

**Response**:

```json
{
  "message": "Tweet deleted"
}
```

#### `PUT /tweets/{tweetId}/like`

**Response**:

```json
{
  "message": "Tweet liked"
}
```

#### `DELETE /tweets/{tweetId}/like`

**Response**:

```json
{
  "message": "Like removed"
}
```

#### `PUT /tweets/{tweetId}/comment`

**Request**:

```json
{
  "content": "Nice tweet!"
}
```

**Response**:

```json
{
  "message": "Comment added",
  "commentId": 5
}
```

#### `DELETE /tweets/{tweetId}/comment/{commentId}`

**Response**:

```json
{
  "message": "Comment deleted"
}
```



* * *

## 🧪 Testing

Use Postman to test the above endpoints with required authorization headers where needed.

## 📜 License

MIT or as applicable.
