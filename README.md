# google-oauth-demo
This utility demonstrates how Google OAuth can be implemented using Google OAuth 2.0 client. The application scope has been strictly restricted to the usage of GMail service. This implies that the OAuth 2.0 use cases that could be performed through this application are GMail specific.
Functioning:
- This utility will assume the following statements to be true :
    - The user has created a Google cloud console project.
    - The user has enabled GMail API for the console project.
    - The user has created OAuth 2.0 client that represents this utility application.
    - All these tasks are done using the gmail id, that user provides during onboarding phase.
        - This ensures that the mail id to be operated on has proper authorization. If not the same, the email id should be already added to the list of test users, as this is an "External" type of application.