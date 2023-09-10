#!/bin/bash

echo 'Starting to Deploy...'

# Install required dependencies
sudo apt update
wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
sudo add-apt-repository 'deb https://apt.corretto.aws stable main'
sudo apt update
sudo apt install -y java-17-amazon-corretto-jdk
sudo apt install -y nginx
#sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
#yes | curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
#sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
#apt-cache policy docker-ce
#sudo apt install -y docker-ce

# make sure stocktrade docker is not running
#sudo docker rm $(sudo docker stop $(sudo docker ps -a -q --filter ancestor=stocktrade:latest --format="{{.ID}}"))

# copy nginx conf to default
sudo cp auth.conf /etc/nginx/sites-available/

sudo ln -s /etc/nginx/sites-available/auth.conf /etc/nginx/sites-enabled/

sudo unlink /etc/nginx/sites-enabled/default

sudo nginx -t

sudo systemctl restart nginx

# build dockerfile
#sudo docker build -f Dockerfile -t stocktrade:latest .

# run in detached mode
#sudo docker run -p 8080:8080 -d stocktrade:latest

/opt/apache-maven-3.8.7/bin/mvn clean compile package
sudo deluser authu
sudo useradd authu
sudo chown authu:authu /home/ubuntu/auth-api/target/stocktrade-0.1.jar
sudo chmod 500 /home/ubuntu/auth-api/target/stocktrade-0.1.jar
sudo chown authu:authu /home/ubuntu/logs
sudo cp auth.service /etc/systemd/system
sudo systemctl enable auth.service
sudo systemctl start auth.service

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