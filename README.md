# BingeNow
TV Show recommendation web-app using TVMaze API, Java, Postgres, Javascript

Design Stack:

1.  Backend - Java
2.  Frontend - Javascript
3.  Database - Postgres

Java Package Structure:
  1.  Client
      1.  UserServlet
      2.  ShowServlet
  2.  Server
      1.  User Package
          1.  UserHandler
          2.  UserUtil
      2.  Show Package
          1.  ShowHandler
          2.  ShowUtil
      3.  Database Pacakge
          1.  DataAccess
          2.  DatabaseUtil
          3.  DBConnection


Database Table Structure:

1.  User (AAAUSER)
    1.  user_id   (SERRIAL) [PRIMARY KEY]
    2.  username  (VARCHAR)
    3.  password  (VARCHAR)

2.  Show (AAASHOW)
    1.  show_id   (SERIAL) [PRIMARY KEY]
    2.  show_name (VARCHAR)
    3.  remote_show_id (INT)

3.  Recommendation (AAARECOMMENDATION)
    1.  recommendation_id (SERIAL) [PRIMARY KEY]
    2.  user_id           (INT) [FOREIGN KEY]
    3.  show_id           (INT) [FOREIGN KEY]
    4.  watchlist         (BOOLEAN)
