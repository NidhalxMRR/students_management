pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'nidhalgharbiii/students-management:1.0.0'
    }
    stages {
        stage('Checkout Code') {
            steps {
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
                sh 'mvn test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=students-management -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh "DOCKER_BUILDKIT=0 docker build -t ${DOCKER_IMAGE} ."
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
        stage('Kubernetes Deploy') {
            steps {
                sh '''
                    kubectl apply -f k8s/mysql-deployment.yaml
                    sleep 20
                    kubectl apply -f k8s/spring-deployment.yaml
                    kubectl rollout status deployment/spring-app -n devops --timeout=90s
                '''
            }
        }
    }
    post {
        success {
            echo 'Pipeline OK ! App deployée sur Kubernetes - namespace devops.'
        }
        failure {
            echo 'Pipeline échoué !'
        }
    }
}
