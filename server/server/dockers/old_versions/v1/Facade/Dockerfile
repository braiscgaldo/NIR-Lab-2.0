FROM ubuntu:20.10

# update and upgrade image
RUN apt-get update -y
RUN apt-get upgrade -y
RUN apt-get update -y

# installing python and dependencies
RUN apt install python3.9 -y
RUN apt install python3-pip -y

# installing mariadb dependencies
RUN apt install libmysqlclient-dev python-dev -y


# copy files
COPY Facade.tar.gz /opt

# extracting files and removing files
RUN cd /opt && tar -xf Facade.tar.gz && rm Facade.tar.gz

# installing python dependencies
RUN cd /opt/ && pip3 install -r requirements.txt

# Python path
ENV PYTHONPATH :/opt/src

# running docker
CMD [ "/bin/bash" ]
