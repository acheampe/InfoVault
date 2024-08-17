from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route('/')
def home():
    return "Welcome to the Document Management Service!"

# Define more routes here for document CRUD operations

if __name__ == '__main__':
    app.run(debug=True)
