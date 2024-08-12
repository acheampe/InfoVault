from fastapi import FastAPI
import json
import uvicorn

app = FastAPI()

# Load the JSON data
with open('infovault_api_endpoints.json', 'r') as file:
    api_endpoints = json.load(file)

# Loop through the API endpoints to create routes dynamically
for service in api_endpoints['api_endpoints']:
    for endpoint in service['endpoints']:
        if endpoint['method'] == 'POST':
            @app.post(endpoint['endpoint'])
            async def dynamic_post_route():
                return endpoint['response_body']
        elif endpoint['method'] == 'GET':
            @app.get(endpoint['endpoint'])
            async def dynamic_get_route():
                return endpoint['response_body']
        elif endpoint['method'] == 'PUT':
            @app.put(endpoint['endpoint'])
            async def dynamic_put_route():
                return endpoint['response_body']
        elif endpoint['method'] == 'DELETE':
            @app.delete(endpoint['endpoint'])
            async def dynamic_delete_route():
                return endpoint['response_body']

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)
