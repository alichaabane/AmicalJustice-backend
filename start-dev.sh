docker rmi localhost:5000/justice-backend:1.0.0
ENV_FILE=./.env.dev docker-compose up -d  --force-recreate
#ENV_FILE=./.env.dev docker-compose up -d --build
