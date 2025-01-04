# **LMS Project: CI/CD Pipeline and Deployment Process Documentation**

## **1. Project Overview**

### **1.1 Context**
The architecture of the LMS Project is decentralized and distributed. The system will be made up of many different applications, which may each run on more than one machine. Each application evolves independently of others, regarding code and deployment. Such independent evolution may bring in integration and interface issues in applications interacting with each other in the system.

### **1.2 Goal**
The goal of this sprint/project is to support the independent release and deployment of each application, detecting any integration problems that occur between applications.

## **2. Requirements**

### **2.1 Non-Functional Requirements**
- **Performance**: The system must increase performance by 25% when under high demand (i.e., more than Y requests per period).
- **Hardware Efficiency**: The system must use hardware resources efficiently, especially during peak loads (when requests are > Y per period).
- **Releasability**: Despite the decentralized and independent development of applications, each application must maintain or improve its releasability.
- **Independent Deployment**: Each application must be independently deployable, despite the decentralized nature of development.
- **Environments**: Development, Testing, and Production environments should be adopted.
- **Automatic Rollback**: The system should allow automatic rollback of any service to a previous version.
- **Zero Downtime Updates**: The system should not experience downtime when updating a service.

### **2.2 Functional Requirements**
- **User Notification**: The user triggering the pipeline must receive an email or another type of notification with a link to the deployed service (not the production version) for acceptance or rejection.

## **3. CI/CD/CD Pipeline Development**


## **4. Performance Proofs**

To ensure that the LMS system meets the required performance standards, **JMeter** will be used to simulate various load conditions and evaluate the behavior of the system under stress. Performance testing is crucial for identifying bottlenecks, ensuring scalability, and validating the system's ability to handle high traffic without failure.

### **Types of Performance Tests:**

1. **Load Testing**:
   - **Objective**: To test how the system performs under expected load conditions (e.g., during peak hours).
   - **Approach**: Simulate normal usage by generating a steady stream of requests that reflect typical user activity.
   - **Goal**: Ensure the system can handle the anticipated number of concurrent users or transactions without performance degradation.

2. **Stress Testing**:
   - **Objective**: To push the system beyond its normal operational capacity to determine its breaking point.
   - **Approach**: Gradually increase the number of requests to find the maximum load the system can handle before it begins to degrade or fail.
   - **Goal**: Identify the system's limits and ensure graceful degradation under extreme load.

3. **Endurance (Soak) Testing**:
   - **Objective**: To test the system’s stability over an extended period of time under a constant load.
   - **Approach**: Run tests for several hours or days to simulate long-term usage under steady traffic.
   - **Goal**: Detect memory leaks, resource exhaustion, or other issues that may arise over time, ensuring the system remains reliable during prolonged operation.

## **5. Conclusion**
This document outlines the goals, requirements, and steps to implement a robust and reliable CI/CD/CD pipeline for the LMS project, ensuring the independent deployment and high availability of microservices while maintaining efficient resource usage and performance during peak demands.
