import docker
import pydocker


class Dockers:
    """
    Class representing a docker
    """

    def __init__(self, docker_info, client):
        self.__docker = docker_info
        self.__client = client

    def launch_docker(self):
        """
        Method for launch a docker
        :return: docker launched
        """
        return self.__client.containers.run(image=self.__docker.image, command=str(self.__docker), auto_remove=True,
                                            working_dir='/opt/src/scripts/', links=self.__docker.links,
                                            volumes=self.__docker.volumes)


class DockerInterface:
    """
    Class for representing an interface to docker. Receives a docker interface to launch.
    """

    def __init__(self):
        self.__dockers = {'data_treatment': [], 'training': []}
        self.__client = docker.from_env()

    def _launch_dockers(self, docker_info):
        """
        Method for launch a docker
        :return: docker launched
        """
        if 'training' in docker_info.image:
            self.__dockers['training'].append(Dockers(docker_info, self.__client).launch_docker())
            print(self.__dockers['training'])
        else:
            self.__dockers['data_treatment'].append(Dockers(docker_info, self.__client).launch_docker())
            print(self.__dockers['data_treatment'])

    def main(self, docker_info):
        """
        Main method of the object
        :return:
        """
        self._launch_dockers(docker_info)


if __name__ == '__main__':
    print(pydocker)
