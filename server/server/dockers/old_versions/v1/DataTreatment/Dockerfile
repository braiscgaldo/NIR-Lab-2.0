FROM ubuntu:20.10

# update and upgrade image
RUN apt update -y
RUN apt upgrade -y

# installing python and dependencies
RUN apt install python3 -y
RUN apt install python3-pip -y

# installing mariadb dependencies
RUN apt install libmysqlclient-dev python-dev -y



# copy files
COPY DataTreatment.tar.gz /opt

# extracting files and removing files
RUN cd /opt && tar -xf DataTreatment.tar.gz && rm DataTreatment.tar.gz

# installing python dependencies
RUN cd /opt/ && pip3 install -r requirements.txt

# running docker
CMD [ "/bin/bash" ]

