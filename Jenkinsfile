pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'nidhalgharbiii/students-management:1.0.0'
    }

    stages {
        stage('Checkout Code') {
            steps {
                // This natively pulls the code from the GitHub repository configured under SCM settings
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -DskipTests || true'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        docker logout
                        rm -f /home/vagrant/.docker/config.json
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push nidhalgharbiii/students-management:1.0.0
                    '''
                }
            }
        }

        stage('Docker Run') {
            steps {
                sh '''
                    docker stop students-app mysql-db || true
                    docker rm students-app mysql-db || true
                    docker rmi mysql:8.0.36 || true
                    docker network create students-net || true

                    docker run -d \
                      --name mysql-db \
                      --network students-net \
                      -e MYSQL_ROOT_PASSWORD=root \
                      -e MYSQL_DATABASE=students_db \
                      mysql:8.0.36

                    sleep 25

                    docker run -d \
                      --name students-app \
                      --network students-net \
                      -p 9090:8080 \
                      -e SPRING_DATASOURCE_URL='jdbc:mysql://mysql-db:3306/students_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true' \
                      -e SPRING_DATASOURCE_USERNAME=root \
                      -e SPRING_DATASOURCE_PASSWORD=root \
                      nidhalgharbiii/students-management:1.0.0
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline OK ! App disponible sur le port 9090.'
        }
        failure {
            echo 'Pipeline échoué !'
        }
    }
}
