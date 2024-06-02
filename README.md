# Issue Tracking System

## Description
이 프로젝트는 이슈를 관리하고 추적할 수 있는 Issue Tracking System입니다. 이 시스템은 이슈를 생성, 업데이트, 삭제하는 기능과 더불어 트렌드와 이슈 를 분석할 수 있는 통계 기능을 제공합니다.


## Features
- 이슈 생성, 업데이트 및 삭제
- 사용자에게 이슈 할당
- 이슈 우선순위 설정 (Blocker, Critical, Major, Minor, Trivial)
- 월별, 연도별 이슈 통계 생성
- 키워드로 이슈 검색


### 사전 조건
- 실행환경 : Window 10 pro
openjdk version "17.0.6" 2023-01-17
OpenJDK Runtime Environment Temurin-17.0.6+10 (build 17.0.6+10)
OpenJDK 64-Bit Server VM Temurin-17.0.6+10 (build 17.0.6+10, mixed mode, sharing)
- Min JRE version : 1.8.0 이상.


### 실행방법 
1. FrontEnd-jar_최종.exe & BackEnd-jar_최종_jar_exe 파일 준비.

2. 프로젝트 디렉토리로 이동 : ex ) cmd -> cd C:\Users\user\Desktop\BackEnd-jar_최종_jar_exe\BackEnd-jar

3. 실행 명령어 입력 : >java -jar IssueTrackingSystem-0.0.1-SNAPSHOT.jar

4. FrontEnd-jar_최종.exe 파일 이동 

5. SoftwareEngineering.exe 실행.


#### 사용법 (Usage)
프로젝트의 주요 기능을 사용하는 방법을 설명합니다. 

- Connect URL 창에서는 연결하고 싶은 URL을 입력합니다. (아무것도 입력하지 않을 시 Default로 localhost:8080로 접속)
- 그 후 로그인 : ADMIN계정 - ID : ADMIN : PW: 0000
   	             PL 계정 - ID : 정재민 PW : 5555 , ID : 송영범 PW : 3333
                       DEV 계정 - ID : 박재현 PW : 2222 , ID : 이민호 PW : 4444
                   TESTER 계정 - ID : 김진호 PW :  1111 
  
- 계정 추가 
1. Login 창에서 Sign Up 버튼을 클릭합니다.
2. Name, ID, Password를 입력한 후 Submit 버튼을 클릭합니다. 

- 이슈 브라우즈 및 검색 (예를 들어 assignee 값, 이슈 상태, reporter 값 등)
1. 이슈를 목록 창에서 "search" 필드에 검색어를 입력합니다.
2. "search" 버튼을 클릭합니다.
3. 조건에 맞는 issue의 assignee값, 이슈 상태, reporter 값 등 출력됩니다.

- 이슈 등록 (꼭 채워야하는 필드는 Title, Description. reporter 필드는 이슈를 등록한 계정으로, reported date는 등록한 날짜와 시간으로 자동으로 채워짐)
1. "+new issue" 버튼을 클릭
2. 원하는 issue의 title, priority, description을 입력.
3. 조건에 맞게 issue 생성이 완료됩니다.

- 이슈 코멘트 추가
1. issue 목록 창에서 코멘트를 추가하고 싶은 issue를 선택하여 클릭합니다.
2. 오른쪽 comment창에서 comment를 입력한 후 "send"버튼을 클릭합니다.

- 이슈 상세 정보 확인 (이슈의각 필드와 코멘트 history 확인 가능)
1. 이슈 목록에서 이슈를 클릭합니다.
2. 이슈의 상세 정보 화면이 표시되며, 각 필드와 코멘트 history 확인이 가능합니다.

- 이슈 배정을 포함한 이슈 상태 변경
1. 이슈 상세 화면에서 각 목록 창에서 Assignee , Priority를 설정합니다. (Assignee가 issue fix가능)
2. issue가 Assigned , fixed 될때마다 이슈의 상태가 변경됩니다.

- 이슈 통계 분석(일/월 별 이슈 발생 횟수 및 트랜드 등 표시)
1. 이슈 목록에서 "statistics"버튼을 클릭합니다.
2. 연도에 대한 이슈 통계 그래프가 표시됩니다. 
