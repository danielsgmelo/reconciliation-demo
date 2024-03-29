# FROM registry.access.redhat.com/quarkus/mandrel-21-jdk17-rhel8:21.3-40
FROM registry.access.redhat.com/quarkus/mandrel-22-rhel8:22.3-6

USER root

RUN yum -y update && \
    yum -y install git wget sudo zip

# # mvn
ARG MAVEN_VERSION=3.8.7
# ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref && \
    curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 && \
    rm -f /tmp/apache-maven.tar.gz && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
# ENV MAVEN_CONFIG "/workspaces/reconciliation-demo/.m2"

# ADD settings.xml /usr/share/maven/conf/

ARG USERNAME=user
ARG USER_UID=1000
ARG USER_GID=$USER_UID
ARG DOCKER_GID=1001

# Create the user
RUN userdel quarkus && \
    groupadd --gid $USER_GID $USERNAME && \
    useradd --uid $USER_UID --gid $USER_GID -m $USERNAME -s /bin/bash && \
    echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME && \
    chmod 0440 /etc/sudoers.d/$USERNAME

# docker
RUN wget https://download.docker.com/linux/static/stable/x86_64/docker-20.10.2.tgz && \
    tar xzvf docker-20.10.2.tgz && \
    cp docker/* /usr/bin/ && \
    groupadd --gid $DOCKER_GID docker && \
    usermod -aG docker $USERNAME

USER $USERNAME

RUN bash -c "$(curl -fsSL https://raw.githubusercontent.com/ohmybash/oh-my-bash/master/tools/install.sh)"

# install quarkus cli
RUN curl -Ls https://sh.jbang.dev | bash -s - trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/ && \
    curl -Ls https://sh.jbang.dev | bash -s - app install --fresh --verbose --force quarkus@quarkusio

# RUN echo 'shopt -s histappend' >> /home/user/.bashrc && \
#     echo 'PROMPT_COMMAND=history -a;$PROMPT_COMMAND' >> /home/user/.bashrc && \
#     sed -i 's/OSH_THEME="font"/OSH_THEME="powerline-plain"/g' /home/user/.bashrc && \
#     echo 'HISTFILE=/workspaces/reconciliation-demo/.bash_history' >> /home/user/.bashrc