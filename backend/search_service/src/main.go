package main

import (
    "github.com/gin-gonic/gin"
)

func main() {
    r := gin.Default()

    // Define your routes here
    r.GET("/search", func(c *gin.Context) {
        c.JSON(200, gin.H{
            "message": "Search Service is up and running!",
        })
    })

    // Start the server
    r.Run(":8080") // Listen and serve on port 8080
}
