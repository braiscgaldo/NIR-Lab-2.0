FROM ubuntu:20.10

# update and upgrade image
RUN apt update -y
RUN apt upgrade -y

# installing python and dependencies
RUN apt install python3 -y
RUN apt install python3-pip -y


# copy files
COPY Training.tar.gz /home

# extracting files and removing files
RUN cd /home && tar -xf Training.tar.gz && rm Training.tar.gz

# installing python dependencies
RUN cd /home/ && pip3 install -r requirements.txt

# running docker
CMD [ "/bin/bash" ]

