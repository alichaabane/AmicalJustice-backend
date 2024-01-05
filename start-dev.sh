docker rmi justice-backend:v1
ENV_FILE=./.env.dev docker-compose up -d  --force-recreate
#ENV_FILE=./.env.dev docker-compose up -d --build
