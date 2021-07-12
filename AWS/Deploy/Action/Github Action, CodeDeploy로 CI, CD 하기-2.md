# `Github Action, CodeDeploy로 CI/CD 하기 - 2편`

```yaml
```yaml
name: gyunny-action

on:
  push:
    branches:
      - master
  workflow_dispatch:

env:
  S3_BUCKET_NAME: aws-gyun-s3

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Set up JDK 11
        uses: actions/setup-java@v1
        with:
          java-version: 11

      - name: Grant execute permission for gradlew
        run: chmod +x ./gradlew
        shell: bash

      - name: Build with Gradle
        run: ./gradlew build
        shell: bash
        
      - name: Make zip file
        run: zip -r ./$GITHUB_SHA.zip .
        shell: bash

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ secrets.AWS_REGION }}

      - name: Upload to S3
        run: aws s3 cp --region ap-northeast-2 ./$GITHUB_SHA.zip s3://$S3_BUCKET_NAME/$GITHUB_SHA.zip

      - name: Code Deploy
        run: aws deploy create-deployment --application-name logging-system-deploy --deployment-config-name CodeDeployDefault.OneAtATime --deployment-group-name develop --s3-location bucket=$S3_BUCKET_NAME,bundleType=zip,key=$PROJECT_NAME/$GITHUB_SHA.zip
```
```