#!/bin/bash

echo 'Starting to Deploy...'

# Install required dependencies
sudo apt update
wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
sudo add-apt-repository 'deb https://apt.corretto.aws stable main'
sudo apt update
sudo apt install -y java-17-amazon-corretto-jdk
#sudo wget https://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz
#sudo tar -xvf apache-maven-3.9.4-bin.tar.gz
#sudo mv apache-maven-3.9.4 /opt/
sudo apt install -y nginx

# copy nginx configuration files to respective paths
sudo cp self-signed.conf /etc/nginx/snippets/
sudo cp ssl-params.conf /etc/nginx/snippets/
sudo cp spring-auth.conf /etc/nginx/sites-available/

sudo ln -s /etc/nginx/sites-available/spring-auth.conf /etc/nginx/sites-enabled/

sudo unlink /etc/nginx/sites-enabled/default

sudo nginx -t

sudo systemctl restart nginx

sudo ufw allow 'Nginx Full'

# build dockerfile
#sudo docker build -f Dockerfile -t spring-auth:latest .

# run in detached mode
#sudo docker run -p 8080:8080 -d spring-auth:latest
sudo deluser spring-auth
sudo useradd spring-auth
sudo chown spring-auth:spring-auth /home/ubuntu/logs
./mvnw clean compile package  -DskipTests

sudo chown spring-auth:spring-auth /home/ubuntu/spring-auth/target/spring-auth-0.1.jar
sudo chmod 500 /home/ubuntu/spring-auth/target/spring-auth-0.1.jar
sudo cp spring-auth.service /etc/systemd/system
sudo systemctl enable spring-auth.service
sudo systemctl start spring-auth.service

sleep 15

PORT=80
checkHealth() {
    PORT=$1
    url="http://$HOSTNAME:$PORT/actuator/health"

    pingCount=0
    stopIterate=0
    loopStartTime=`date +%s`
    loopWaitTime=150 ## in seconds

    # Iterate till get 2 success ping or time out
    while [[ $pingCount -lt 2 && $stopIterate == 0 ]]; do
        startPingTime=`date +%s`
        printf "\ncurl -m 10 -X GET $url"
        curl -m 10 -X GET $url -o /dev/null 2>&1
        returnCode=$?
        if [ $returnCode = 0 ]
            then
            pingCount=`expr $pingCount + 1`
        fi
        endPingTime=`date +%s`
        pingTimeTaken=`echo " $endPingTime - $startPingTime " | bc -l`
        loopEndTime=`date +%s`
        loopTimeTaken=`echo " $loopEndTime - $loopStartTime " | bc -l`

        echo "Ping time is " $pingTimeTaken
        echo "ReturnCode is $returnCode"
        echo "PingCount is $pingCount "

        waitTimeEnded=`echo "$loopTimeTaken > $loopWaitTime" | bc -l`
        echo "LoopTimeTaken is $loopTimeTaken"
        echo "WaitTimeEnded is $waitTimeEnded"
        # On timeout, if 2 successfully pings not received, stop interaction
        if [[ $pingCount -lt 2 && "$waitTimeEnded" -eq 1 ]];
            then
            stopIterate=1
        fi
        sleep 5
    done

    if [ $stopIterate -eq 1 ]
    then
        if [ $pingCount -lt 2 ]
        then
            echo "PingCount is less than 2"
        else
            echo "Time taken in building took more than $loopWaitTime seconds"
        fi

        exit 1
    fi
}


checkHealth $PORT
checkHealthResponse=$?
if [ checkHealthResponse = 1 ]
    then
        echo "CheckHealth returns 1 that means something went wrong, exiting..."
        exit 1
else
    printf "\n\nService is running on $PORT ...\n\n"
fi

echo 'Deployment completed successfully'
exit 0