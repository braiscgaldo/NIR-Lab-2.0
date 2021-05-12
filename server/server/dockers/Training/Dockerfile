FROM ubuntu:20.10

# update and upgrade image
RUN apt update -y
RUN apt upgrade -y

# installing python and dependencies
RUN apt install python3 -y
RUN apt install python3-pip -y

# install requirements
COPY requirements.txt /opt

# installing python dependencies
RUN cd /opt/ && pip3 install -r requirements.txt

# copy files
COPY Training.tar.gz /opt

# extracting files and removing files
RUN cd /opt && tar -xf Training.tar.gz && rm Training.tar.gz

# expose ports
EXPOSE 5050

# Running flask environment
WORKDIR /opt/src/scripts

# running docker
CMD [ "flask", "run", "--host", "0.0.0.0", "--port", "5050" ]

