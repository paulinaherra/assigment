db.createUser(
    {
      user: "myuser",
      pwd: "password",
      roles: [
        {
          role: "readWrite",
          db: "mydatabase"
        }
      ]
    }
);