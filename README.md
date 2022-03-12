![image](https://img.shields.io/github/issues/HrishabhPurohit/google-oauth-demo)  ![image](https://img.shields.io/badge/maven--build-passing-green)  ![image](https://img.shields.io/badge/contributors-1-green?url=https://github.com/HrishabhPurohit/google-oauth-demo/graphs/contributors) ![image](https://img.shields.io/github/stars/HrishabhPurohit/google-oauth-demo)  ![image](https://img.shields.io/twitter/url?label=Follow%20%40HrishabhPurohit&style=social&url=https%3A%2F%2Ftwitter.com%2FHrishabhPurohi1)

# Gmail scraping using Google OAuth 2.0
This utility demonstrates how Google OAuth can be implemented using Google OAuth 2.0 client. The application scope has been strictly restricted to the usage of GMail service. This implies that the OAuth 2.0 use cases that could be performed through this application are GMail specific.

# Functioning:
- This utility will assume the following statements to be true :
    - The user has created a Google cloud console project.
    - The user has enabled GMail API for the console project.
    - The user has created OAuth 2.0 client that represents this utility application.
        - Please follow this article to get more context on how to create a OAuth 2.0 client: https://hrishabhpurohittech.blogspot.com/2022/01/google-oauth-20-securing-your-mail.html
        - Generally a client is specific to a web application.
    - All the mail operations are performed on the gmail id that user provides with API request (Read through to understand how to use this utility to read mails).
        - For seamless authroization:
            - Use your personal gmail id.
            - If not your personal gmail id, use a gmail id to which you have access to and which is added as a "Test User" while create the Consent Screen in the previous step. This is needed as this application is an "External" type in that it can be accessed by global gmail users.

    # Assuming all the expectations are met, here is how to operate this utility:
    The implementation is agnostic of the OAuth 2.0 provider:
    - Given the email domain of the user, the utility tries to guess the Email Provider. This guess is based on a preconfigured mapping.
    - For ex: xxxx.xx@gmail.com, where gmail.com is the domain name, the Email Provider is Google's Gmail.
    - Likewise for xxx.xx@outlook.com, where outlook.com is the domain name, the Email Provider is Microsoft's Outlook.

    NOTE: While it is a best practice to have an application distributed into multiple modules handling mutually exclusive components, this utility has every component packaged into single module. This is purely for the sake of convenience.

    Coming to the real implementation details to understand how to operate the utility.

    This is a Spring Boot RESTful application that exposes following 2 endpoints:
    - GET /api/demo/google-oauth/supportedGoogleAPIs

    ![image](https://user-images.githubusercontent.com/36987862/156873709-309604bc-3dc1-45c8-b5be-eaad10757e21.png)

        - Gets all the Google APIs that were enabled by the user to be used through this OAuth 2.0 client.
    - POST /api/demo/google-oauth/action

    ![image](https://user-images.githubusercontent.com/36987862/156873895-20146a60-aaff-4329-8b6f-2cdd06290d51.png)

        - Performs the desired action on the given Google API with the given scopes for the given user in the request body.
        NOTE: OAuth scope is never supposed to come as an input from a requesting client, as is being used above, it is bound to the OAuth 2.0 client consent screen at the time of creation.
    
    This is a high level architecture of the application:

    ![image](https://user-images.githubusercontent.com/36987862/156881590-e04ac17c-9e24-47ff-a194-d48d8f365ccf.png)

    To run the utility:

    1. Clone the repo and make sure local build passes with all the maven dependencies.
    2. Change application.properties according to your local environment.
        - "logging.file.name" : Absolute path including the file name to be created for the app logging.
        - "google.oauth.approach" : Approach you want to take for implementing OAuth flow. This requires preconfigurations. As of now we are following OAuth 2.0 client approach as shown in the above mentioned article.

        NOTE: The second approach is the Service Account approach which is a tricky one. It will come with the second version release for the application.
        
        - "google.oauth.approach.credfile.path" : JSON file downloaded from the https://console.cloud.google.com/ after creating an OAuth 2.0 client
        - "google.oauth.demo.app.request.caching" : True if you dont want to make a Gmail API call for the same operation for the same user with the same scope.

    3. Deploy the spring boot application. A small caveat here:
        - This application needs a "javaagent" VM argument to run. Generally, Java applications (except Spring applications with DevTools dependency) do not need any java agent to launch them.
        - Although this application needs instrumentation in some places so that the compiled class files could be alterred at the time of class loading.
        - To learn more about the Java Instrumentation API and how do we instrument using a java agent, please read this: 

            https://dzone.com/articles/java-agent-1 

            https://www.baeldung.com/java-instrumentation
    4. To launch the application, you will need 2 external jar files, here is where you can download them from (make sure that you download the exact mentioned versions here):

        https://search.maven.org/remotecontent?filepath=org/springframework/spring-instrument/5.3.15/spring-instrument-5.3.15.jar

        https://search.maven.org/remotecontent?filepath=org/aspectj/aspectjweaver/1.9.7/aspectjweaver-1.9.7.jar

        - Add these jar files in the "-javaagent" VM argument in your launch command like this:

        java '-javaagent:<path_to_jar>\spring-instrument-5.3.15.jar' '-javaagent:<path_to_jar>\aspectjweaver-1.9.7.jar' '-cp' '<path_to_application_jar>' 'com.hporg.demo.ApplicationLauncher'
    
    5. Hit the POST endpoint to perform desired action:
        - Do not expect the action result to be returned just after hitting the endpoint. If you observed, the OAuth flow is yet to complete.
        - See the IDE console output, where the application is running. There will be a URL printed.
        - Visit the URL on your browser. If you are already logged in with the gmail user, for whom the request is made, this URL will ask you to select the google account or else will ask you to signin.
        - If you are wondering what is OAuth worth if I have to enter the password for the user, I think you should try to hit the endpoint again and see what happens.
        - Surprised right ? The second time it did not even print the URL and just gave the result. To be sure, please check the log file to see if it is actually hitting the GMail API.

    Since this is a demo application, it only has the ability to perform 2 operations on a user's mailbox. These are:

    1. "READ_MAIL" action:
        - This returns the thread id and message id of the first message in the inbox, like this:

        {
            "id": "17f5b432d9fcde37",
            "threadId": "17f5b3d6cde66e21"
        }

    2. "READ_LABEL" action:
        - This returns the name of the first label (filter) listed on your inbox.