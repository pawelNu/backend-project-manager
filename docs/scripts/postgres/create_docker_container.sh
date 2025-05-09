name=project-manager-dev

# build docker image
docker build -t $name .

# run docker container
docker run --name $name -p 5431:5432 -d $name
