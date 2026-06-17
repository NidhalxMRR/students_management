pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'nidhalgharbiii/students-management:1.0.0'
        KUBECONFIG = '/home/vagrant/.kube/config'
    }

    stages {
        stage('Checkout Code') {
            steps {
                cleanWs()

                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    extensions: [[$class: 'CleanBeforeCheckout']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/NidhalxMRR/students_management.git'
                    ]]
                ])
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
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=students-management \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    DOCKER_BUILDKIT=0 docker build -t ${DOCKER_IMAGE} .
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-hub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        docker logout || true
                        rm -f /home/vagrant/.docker/config.json

                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}
                    '''
                }
            }
        }

        stage('Kubernetes Deploy') {
            steps {
                sh '''
                    echo "Checking Minikube status..."
                    minikube status || minikube start

                    echo "Updating Kubernetes context..."
                    minikube update-context

                    echo "Checking Kubernetes cluster..."
                    kubectl get nodes

                    echo "Creating devops namespace if missing..."
                    kubectl create namespace devops --dry-run=client -o yaml | kubectl apply -f -

                    echo "Deploying MySQL..."
                    kubectl apply -f k8s/mysql-deployment.yaml

                    echo "Waiting for MySQL deployment..."
                    kubectl rollout status deployment/mysql -n devops --timeout=180s

                    echo "Deploying Spring Boot app..."
                    kubectl apply -f k8s/spring-deployment.yaml

                    echo "Waiting for Spring Boot deployment..."
                    kubectl rollout status deployment/spring-app -n devops --timeout=180s

                    echo "Kubernetes resources:"
                    kubectl get pods -n devops
                    kubectl get svc -n devops
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