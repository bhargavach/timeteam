# SAP Business Technology Platform — Foundation Solution Design

**Document Version:** 1.0  
**Date:** August 2026  
**Classification:** Customer-Facing — Solution Architecture  
**Prepared By:** SAP BTP Technology Consulting — ANZ  

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Document Purpose and Scope](#2-document-purpose-and-scope)
3. [SAP BTP Platform Overview](#3-sap-btp-platform-overview)
4. [Account Model and Governance Structure](#4-account-model-and-governance-structure)
5. [Runtime Environments](#5-runtime-environments)
6. [Identity and Access Management](#6-identity-and-access-management)
7. [Platform Security Architecture](#7-platform-security-architecture)
8. [Connectivity Architecture](#8-connectivity-architecture)
9. [Network Architecture and Hyperscaler Topology](#9-network-architecture-and-hyperscaler-topology)
10. [Public Cloud to Private Cloud Connectivity](#10-public-cloud-to-private-cloud-connectivity)
11. [SAP Integration Suite — Foundation Service](#11-sap-integration-suite--foundation-service)
12. [ALM, Monitoring and Operations](#12-alm-monitoring-and-operations)
13. [Foundation Sizing and Commercials Guidance](#13-foundation-sizing-and-commercials-guidance)
14. [Implementation Roadmap](#14-implementation-roadmap)
15. [Architecture Decision Register](#15-architecture-decision-register)
16. [Glossary](#16-glossary)
17. [References](#17-references)

---

## 1. Executive Summary

SAP Business Technology Platform (SAP BTP) is SAP's unified cloud platform that enables customers to integrate, extend, and build applications across their SAP and non-SAP landscapes. For customers beginning their SAP BTP journey, a well-designed **Foundation** is the single most important investment — it defines the governance model, security posture, connectivity architecture, and operational framework that all future workloads will inherit.

This document provides a comprehensive Solution Design for the SAP BTP Foundation layer. It is structured to give both business stakeholders and technical architects a shared understanding of what is being built, why architectural decisions have been made, and how the foundation will evolve as the platform scales.

**Key objectives this design addresses:**

| Objective | Design Response |
|---|---|
| Establish a governed, enterprise-grade BTP account structure | Hierarchical Global Account, Directory, and Subaccount model aligned to organizational topology |
| Secure the platform with enterprise identity standards | Centralized IAM via SAP Cloud Identity Services, corporate IdP federation, role-based access control |
| Provide secure connectivity to on-premise SAP systems | SAP Cloud Connector with High Availability, Destination and Connectivity Service pattern |
| Enable private, non-internet connectivity to cloud workloads | SAP BTP Private Link Service leveraging native AWS/Azure private networking |
| Support hybrid and multi-cloud enterprise architectures | Decision framework for AWS Direct Connect, Azure ExpressRoute, and SAP Private Link patterns |
| Ensure operational visibility from Day 1 | SAP Cloud ALM integration, Alert Notification Service, Health Monitoring |

---

## 2. Document Purpose and Scope

### 2.1 Purpose

This Solution Design document serves as the authoritative technical reference for the **SAP BTP Foundation** implementation engagement. It is intended to:

- Define the architectural patterns and design decisions for the BTP Foundation layer.
- Provide a reference architecture for technical implementation teams.
- Document the security and connectivity model for review by the customer's security, network, and architecture governance teams.
- Establish a baseline that subsequent workload onboarding projects will follow.

### 2.2 Scope

**In Scope:**

- SAP BTP Global Account and subaccount governance model
- Runtime environment selection (Cloud Foundry, Kyma, ABAP)
- Identity and Access Management (SAP Cloud Identity Services, XSUAA, trust configuration)
- Platform security architecture and controls
- Connectivity to on-premise SAP systems (Cloud Connector, Connectivity Service, Destination Service)
- Network topology: BTP on hyperscalers (AWS and Azure)
- Public Cloud to Private Cloud connectivity: SAP BTP Private Link Service, AWS PrivateLink, Azure Private Link, AWS Direct Connect, Azure ExpressRoute
- SAP Integration Suite — Cloud Integration and API Management as foundation services
- Operational tooling: SAP Cloud ALM, Alert Notification Service

**Out of Scope:**

- Individual workload application design (extensions, integrations — covered in separate Solution Design documents per workload)
- SAP RISE with SAP / S/4HANA Cloud migration design
- End-user device management and endpoint security
- Customer internal network WAN/MPLS design (referenced but not designed here)

### 2.3 Assumptions

1. The customer holds or will acquire an SAP BTP enterprise agreement (CPEA or BTPEA commercial model).
2. The customer has an existing corporate Identity Provider (Microsoft Entra ID or equivalent).
3. The customer's SAP on-premise landscape (ECC or S/4HANA On-Premise) is the primary integration target.
4. The customer operates workloads in either AWS or Azure (or both) as the primary hyperscaler.
5. All production workloads require private, non-internet connectivity to cloud and on-premise backends.

---

## 3. SAP BTP Platform Overview

### 3.1 What is SAP BTP?

SAP BTP is a platform-as-a-service (PaaS) offering that provides a unified environment for:

- **Integration**: Connecting SAP and non-SAP systems across cloud and on-premise.
- **Extension**: Building side-by-side extensions to SAP applications (S/4HANA, SuccessFactors, Ariba).
- **Analytics**: Accessing and analyzing data from across the SAP landscape.
- **Development**: Building net-new cloud-native applications leveraging SAP and hyperscaler services.
- **AI/ML**: Consuming SAP and third-party AI services through the BTP AI infrastructure.

### 3.2 BTP Pillars

```
┌──────────────────────────────────────────────────────────────────┐
│                        SAP BTP SERVICES                          │
├─────────────┬────────────────┬─────────────────┬────────────────┤
│  DATABASE   │  INTEGRATION   │  EXTENSION &    │  ANALYTICS &  │
│  & DATA     │  & AUTOMATION  │  DEVELOPMENT    │  PLANNING     │
│             │                │                 │               │
│ SAP HANA    │ Integration    │ SAP Build       │ SAP Analytics │
│ Cloud       │ Suite          │ Apps/Work/      │ Cloud         │
│ Data        │ API Mgmt       │ Process         │ SAP Datasphere│
│ Intelligence│ Event Mesh     │ Automation      │               │
│ AI Core     │ SAP BPA        │ Kyma/CF Runtime │               │
├─────────────┴────────────────┴─────────────────┴────────────────┤
│              FOUNDATION LAYER (Scope of this Document)           │
│  Account Model | IAM | Connectivity | Security | ALM | Network  │
└──────────────────────────────────────────────────────────────────┘
```

### 3.3 Hyperscaler Infrastructure Model

SAP BTP does not operate its own data centers for new deployments. It runs **on top of major hyperscaler infrastructure**, with SAP managing the platform layer and the hyperscaler managing the underlying compute, network, and storage. This architectural choice has significant implications:

- **Native private networking** capabilities of the hyperscaler (AWS PrivateLink, Azure Private Link) are available to BTP workloads.
- **Data residency** is determined by the subaccount region, which maps directly to a hyperscaler region.
- **Performance** benefits from hyperscaler global backbone networks.

| Hyperscaler | Selected BTP Regions | Notes |
|---|---|---|
| **AWS** | `eu10` (Frankfurt), `us10` (US East VA), `ap10` (Sydney), `jp10` (Tokyo) | Largest BTP service availability |
| **Microsoft Azure** | `eu20` (Netherlands), `us20` (US West WA), `ap21` (Singapore) | Strong EU data residency option |
| **Google Cloud** | Selected regions | Limited BTP service availability vs. AWS/Azure |

> **Design Recommendation**: For ANZ customers, the primary production region should be `ap10` (AWS Sydney) or `ap21` (Azure Singapore), depending on data residency requirements and hyperscaler preference. A secondary region in the same hyperscaler provides disaster recovery capability.

---

## 4. Account Model and Governance Structure

### 4.1 BTP Account Hierarchy

The BTP account hierarchy is the governance and cost management framework for all services and runtimes. It must be designed before any service is provisioned.

```
Global Account  (SAP Contract Boundary — one per commercial agreement)
│
├── Directory: Corporate Functions
│   ├── Subaccount: Finance-DEV    [ap10 / AWS Sydney]
│   ├── Subaccount: Finance-TEST   [ap10 / AWS Sydney]
│   └── Subaccount: Finance-PROD   [ap10 / AWS Sydney]
│
├── Directory: Supply Chain & Manufacturing
│   ├── Subaccount: SCM-DEV        [ap10 / AWS Sydney]
│   ├── Subaccount: SCM-TEST       [ap10 / AWS Sydney]
│   └── Subaccount: SCM-PROD       [ap10 / AWS Sydney]
│
├── Directory: Platform Foundation (Shared Services)
│   ├── Subaccount: SharedServices-DEV   [ap10]
│   ├── Subaccount: SharedServices-TEST  [ap10]
│   └── Subaccount: SharedServices-PROD  [ap10]
│
└── Directory: DR / Secondary Region (optional)
    └── Subaccount: Foundation-DR  [ap21 / Azure Singapore]
```

> **Note**: The directory structure above is illustrative. It should be adapted to the customer's organizational model (by Business Unit, by Region, or by Program). The key principle is that Subaccounts provide **blast radius isolation** — a misconfiguration or security incident in one subaccount cannot directly affect another.

### 4.2 Subaccount Design Principles

Each subaccount represents a single deployment boundary with the following characteristics:

| Principle | Implementation |
|---|---|
| **One region per subaccount** | Region is immutable post-creation — choose carefully |
| **Environment isolation** | DEV, TEST, PROD are always separate subaccounts (never co-located) |
| **Entitlement ownership** | Each subaccount receives specific service entitlements from the Global Account |
| **Trust configuration** | Each subaccount has its own Trust Configuration (IdP mapping) — centralize via IAS |
| **Network isolation** | Services in different subaccounts cannot communicate directly without explicit configuration |
| **Cost visibility** | BTP cost reporting is available at subaccount granularity |

### 4.3 Shared Services Subaccount Pattern

The **Platform Foundation (Shared Services)** directory hosts services consumed across all other subaccounts:

| Shared Service | Deployed In | Consumed By |
|---|---|---|
| SAP Cloud Identity Services (IAS/IPS) | SAP-managed (tenant) | All subaccounts via trust configuration |
| SAP Cloud Connector | On-premise (customer network) | All subaccounts needing on-premise access |
| SAP Cloud ALM | SAP-managed (tenant) | All subaccounts via agent/service connection |
| Alert Notification Service | SharedServices-PROD | All productive subaccounts |
| SAP Credential Store | SharedServices-PROD | Applications requiring secret storage |
| SAP Private Link Service | Individual subaccounts | Per subaccount requiring private cloud access |

### 4.4 Entitlement and Quota Management

```
Global Account
│  Entitlements (rights to use services) allocated here
│  Quotas (numeric limits) distributed to subaccounts
│
├── Subaccount A  ← receives: [Integration Suite / standard | 1 unit] [HANA Cloud / hana | 64 GB]
└── Subaccount B  ← receives: [Private Link Service / standard | 2 instances]
```

**Commercial model implications:**

| Commercial Model | Quota Behavior | Recommendation |
|---|---|---|
| **BTPEA / CPEA** | Flexible credit-based consumption; soft quotas | Preferred for large enterprise BTP programs |
| **Pay-As-You-Go (PAYG)** | Metered; immediate access; higher unit cost | Suitable for PoC and early exploration |
| **Subscription** | Fixed quota; predictable cost | Suitable for stable, predictable workloads |

---

## 5. Runtime Environments

### 5.1 Overview

BTP provides three runtime environments within a subaccount. Multiple environments can coexist in a single subaccount, but workload placement should be deliberate.

### 5.2 Environment Comparison

| Dimension | Cloud Foundry (CF) | Kyma Runtime | ABAP Environment |
|---|---|---|---|
| **Runtime Model** | Managed PaaS | Kubernetes-native (managed K8s) | ABAP-as-a-Service |
| **Languages** | Polyglot: Java, Node.js, Python, Go (via buildpacks) | Any containerised workload | ABAP (cloud-ready / Steampunk) |
| **Key Framework** | SAP CAP, Spring Boot | Kubernetes, Istio, Serverless (KEDA) | ABAP RESTful Application Programming (RAP) |
| **Service Binding** | CF service marketplace, VCAP_SERVICES | BTP Service Operator (K8s CRD) | ABAP-native APIs |
| **Target Use Cases** | CAP extensions, OData APIs, Integration backends | Microservices, event-driven, container workloads | S/4HANA Cloud extensions, clean-core ABAP |
| **Operational Complexity** | Low-Medium (SAP managed PaaS) | Medium-High (K8s skills required) | Low (ABAP developer-centric) |
| **CI/CD Tooling** | CF CLI, SAP BTP CLI, SAP CICD Service | kubectl, Helm, kapp-controller | abapGit, ABAP Test Cockpit |

### 5.3 Runtime Selection Guidance

```
Does the workload require SAP-delivered ABAP or clean-core S/4HANA extension?
  └── YES → ABAP Environment (Steampunk)

Does the workload require container-level control, Kubernetes, or complex microservices?
  └── YES → Kyma Runtime

Default for SAP CAP applications, OData services, Fiori backends, or lightweight APIs?
  └── YES → Cloud Foundry Environment
```

> **Foundation Recommendation**: For an initial BTP journey, **Cloud Foundry** is the recommended starting environment due to its lower operational overhead and alignment to SAP CAP. Kyma should be introduced when containerised workloads or Kubernetes expertise are available within the customer team.

---

## 6. Identity and Access Management

### 6.1 IAM Architecture Overview

SAP BTP IAM is built around **SAP Cloud Identity Services** — a managed tenant service comprising the **Identity Authentication Service (IAS)** and the **Identity Provisioning Service (IPS)**. These are supplemented by the **Authorization and Trust Management Service (XSUAA)** within each subaccount.

```
╔══════════════════════════════════════════════════════════════════╗
║            ENTERPRISE IDENTITY ARCHITECTURE                      ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  [Corporate IdP]          e.g. Microsoft Entra ID / ADFS / Okta ║
║       │                                                          ║
║       │  SAML 2.0 or OIDC (Corporate IdP → IAS Federation)      ║
║       ▼                                                          ║
║  [SAP IAS Tenant]         Identity Authentication Service        ║
║       │  - MFA enforcement                                       ║
║       │  - Conditional access policies                           ║
║       │  - Risk-based authentication                             ║
║       │  SAML 2.0 / OIDC (IAS → BTP Subaccount Trust)           ║
║       ▼                                                          ║
║  [BTP Subaccount]         Trust Configuration (one IAS trust)    ║
║       │                                                          ║
║       │  OAuth 2.0 JWT                                           ║
║       ▼                                                          ║
║  [XSUAA / AMS]            Authorization server, scope/role mgmt  ║
║       │                                                          ║
║       │  Role Collections → User/Group assignments               ║
║       ▼                                                          ║
║  [Application]            Validates JWT, enforces scopes         ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### 6.2 SAP Cloud Identity Services (IAS and IPS)

#### Identity Authentication Service (IAS)

IAS is the **central authentication broker** for all BTP workloads. Each customer receives a dedicated IAS tenant (bundled with BTPEA/CPEA).

| Capability | Description |
|---|---|
| **Authentication** | SAML 2.0, OIDC, OAuth 2.0 — standards-based authentication for BTP and SAP SaaS applications |
| **Corporate IdP Federation** | Acts as a proxy between any number of corporate IdPs and BTP subaccounts |
| **Multi-Factor Authentication (MFA)** | TOTP, email OTP, WebAuthn (FIDO2), optional per application or risk level |
| **Conditional Access / Risk-Based Auth** | Allow/deny based on IP range, device, group membership, or authentication strength |
| **Single Sign-On** | SSO across all BTP subaccounts and SAP cloud applications trusting the same IAS tenant |
| **User Store** | Optional — IAS can manage users internally or delegate entirely to corporate IdP |

#### Identity Provisioning Service (IPS)

IPS automates the **synchronization of users and groups** from the corporate directory into BTP and SAP applications.

| Source System | Target System | Sync Object |
|---|---|---|
| Microsoft Entra ID (AAD) | SAP IAS | Users and Groups |
| SAP IAS | SAP BTP Subaccount | Users |
| SAP IAS | SAP SuccessFactors | Employee records |
| LDAP Directory | SAP IAS | Users and Groups |

> **Design Pattern**: Configure the corporate IdP once in IAS as a **Corporate Identity Provider**. All BTP subaccounts trust IAS only — this ensures a single trust configuration point regardless of how many subaccounts are added in the future.

### 6.3 Authorization Model (XSUAA and Role Collections)

#### XSUAA Architecture

XSUAA is the per-subaccount OAuth 2.0 authorization server. Applications create a bound XSUAA service instance that defines their authorization model via `xs-security.json`.

```
xs-security.json defines:
  └── scopes           (e.g., "PurchaseOrder.Read", "PurchaseOrder.Write")
  └── role-templates   (named sets of scopes)
  └── role-collections (presented in BTP Cockpit for assignment to users/groups)
```

#### Supported OAuth 2.0 Grant Flows

| Flow | Use Case |
|---|---|
| `authorization_code` (+ PKCE) | Interactive user login via browser — SAP Approuter |
| `client_credentials` | Service-to-service / technical user (no human user involved) |
| `jwt-bearer` / `user_token` | Token exchange for principal propagation to backend systems |
| `refresh_token` | Session continuation for interactive users |

#### Role Collection Best Practices

| Practice | Rationale |
|---|---|
| Assign Role Collections to **IdP groups**, not individual users | Scales with organizational changes; group membership managed in AD, not BTP |
| Use IPS to synchronize groups from Entra ID → IAS → BTP | Removes manual provisioning burden |
| Name Role Collections with a clear naming convention | e.g., `BTP_<App>_<Role>` — `BTP_FINAPP_VIEWER`, `BTP_FINAPP_APPROVER` |
| Review Role Collection assignments quarterly | Principle of least privilege; remove stale access |

### 6.4 Principal Propagation

Principal propagation allows the logged-in BTP user's identity to be forwarded to downstream on-premise SAP systems — eliminating the need for shared technical users in backend SAP systems.

```
Full Principal Propagation Flow:

  [User Browser]
       │ Login (SAML/OIDC via IAS)
       ▼
  [Approuter / BTP App]
       │ Holds IAS JWT token
       │ OAuth2JWTBearer token exchange
       ▼
  [BTP Destination Service]
       │ Resolves destination: ProxyType=OnPremise, Auth=PrincipalPropagation
       ▼
  [BTP Connectivity Service]
       │ SOCKS5 proxy — routes through Cloud Connector tunnel
       ▼
  [SAP Cloud Connector]
       │ Extracts user identity from token
       │ Issues short-lived X.509 certificate [CN=<username>]
       ▼
  [SAP S/4HANA / ECC On-Premise]
       │ Receives X.509 cert; maps CN to SAP user via SNC trust
       ▼
  [Backend executes under end-user context]
```

**Configuration prerequisites for principal propagation:**

| System | Configuration Required |
|---|---|
| BTP Destination | `Authentication = PrincipalPropagation`, `ProxyType = OnPremise` |
| SAP Cloud Connector | Principal Propagation enabled; Subject Pattern: `CN=${name}` |
| SAP Backend (S/4HANA/ECC) | SCC CA certificate imported; system certificate trust configured in transaction STRUST |

### 6.5 IAM Governance Checklist

- [ ] IAS tenant provisioned and corporate IdP federation configured
- [ ] IPS source and target systems configured; user/group sync tested
- [ ] All productive subaccounts trust IAS (not SAP ID Service default)
- [ ] MFA enforced for all human users accessing productive subaccounts
- [ ] Role Collections defined per application with `BTP_<App>_<Role>` naming convention
- [ ] Role Collections assigned to Entra ID/LDAP groups (not individuals)
- [ ] Global Account Administrator role restricted to a maximum of 3 named individuals
- [ ] Emergency break-glass admin procedure documented

---

## 7. Platform Security Architecture

### 7.1 Security Layered Model

SAP BTP security operates across multiple layers. Each layer has distinct controls:

```
┌─────────────────────────────────────────────────────────┐
│  LAYER 5: APPLICATION SECURITY                          │
│  Token validation, scope enforcement, input validation  │
├─────────────────────────────────────────────────────────┤
│  LAYER 4: AUTHORIZATION (XSUAA / AMS)                  │
│  Role collections, OAuth2 scopes, principal propagation │
├─────────────────────────────────────────────────────────┤
│  LAYER 3: AUTHENTICATION (IAS / Corporate IdP)         │
│  MFA, conditional access, SSO, SAML/OIDC               │
├─────────────────────────────────────────────────────────┤
│  LAYER 2: PLATFORM SECURITY (SAP-managed)              │
│  Audit logging, credential management, mTLS, DDoS      │
├─────────────────────────────────────────────────────────┤
│  LAYER 1: INFRASTRUCTURE SECURITY (Hyperscaler)        │
│  Physical security, network isolation, encryption       │
└─────────────────────────────────────────────────────────┘
```

### 7.2 Audit Logging

The **SAP Audit Log Service** is a platform-level service that records all security-relevant operations within a subaccount. It is **mandatory for all productive subaccounts**.

| Event Category | Example Logged Events |
|---|---|
| **Configuration changes** | Trust configuration modified, Role Collection created/deleted |
| **Access management** | User added/removed, Role Collection assigned |
| **Service operations** | Service instance created, service key created/deleted, service binding |
| **Credential events** | Client secret rotation, certificate upload, credential access |
| **Data access** | (Application-level) Custom audit log entries from application code |

> **Compliance Note**: The SAP Audit Log Service retains logs for **90 days by default** in the platform-managed tier. For compliance requirements exceeding 90 days (e.g., ISO 27001, SOC 2, local regulatory requirements), configure log forwarding to a customer-managed SIEM (e.g., Splunk, Microsoft Sentinel) using the Audit Log Management API.

### 7.3 Credential Management

**Guiding principle:** No credentials should be hardcoded in application code or configuration files. All secrets are managed via platform services.

| Credential Type | Storage & Management Approach |
|---|---|
| XSUAA client secret / mTLS cert | SAP Service Binding (injected at runtime via environment variables) |
| Destination credentials (BasicAuth, OAuth token URLs) | SAP Destination Service — stored as destination properties, never in code |
| API keys, passwords, certificates | SAP Credential Store service — vault-like service with RBAC access and audit trail |
| CI/CD pipeline secrets | Customer CI/CD vault (e.g., HashiCorp Vault, Azure Key Vault) — injected at pipeline runtime |

**mTLS over client_secret**: Where the service or library supports it, configure service bindings to use **X.509 certificate authentication (mTLS)** instead of client secrets. mTLS certificates are shorter-lived and remove the risk of long-lived secret exposure.

**Rotation cadence recommendation:**

| Credential | Recommended Rotation |
|---|---|
| XSUAA client secrets | Every 90 days via CI/CD pipeline |
| X.509 service binding certificates | On expiry (typically 1 year); consider 90-day certs |
| Destination basic auth passwords | Every 90 days or on personnel change |
| Cloud Connector system certificate | Annually |

### 7.4 Security Certifications

SAP BTP holds the following certifications relevant to enterprise and regulated customers:

| Certification | Standard | Scope |
|---|---|---|
| **C5** | BSI Cloud Computing Compliance Criteria Catalogue (Germany) | SAP BTP Core Services |
| **SOC 1 / SOC 2 Type II** | AICPA | SAP BTP and SAP Central Cloud Services (annual reports available on SAP Trust Center) |
| **ISO 27001 / 27017 / 27018** | ISO/IEC | Platform operations, cloud security, cloud privacy |
| **PCI DSS** | PCI Security Standards Council | Kyma Runtime (AoC published) |
| **GDPR** | EU GDPR | Data processing agreements available; data residency enforced by region selection |

> Reports are available at [SAP Trust Center](https://www.sap.com/about/trust-center/security.html).

### 7.5 Top 10 BTP Security Recommendations

1. **Centralize authentication in IAS** — configure corporate IdP once in IAS; trust IAS from all subaccounts.
2. **Enforce MFA** for all human users accessing productive subaccounts — configure in IAS application settings.
3. **Assign Role Collections to groups, not individuals** — manage group membership in Entra ID/LDAP.
4. **Apply principle of least privilege** — define narrow XSUAA scopes; do not grant Global Account Administrator broadly.
5. **Enable the Audit Log Service** in every productive subaccount and forward logs to SIEM.
6. **Use mTLS** for service bindings where supported; rotate credentials on a defined schedule.
7. **Restrict Cloud Connector resources** — explicitly whitelist only required ICF paths and RFC function modules; never expose full systems.
8. **Store all secrets in SAP Credential Store** — no hardcoded credentials in destination configurations, code, or git repositories.
9. **Validate token issuer and audience** in every microservice receiving inbound JWTs.
10. **Pipeline-controlled configuration promotion** — trust, role, destination configurations should be promoted via CI/CD pipelines, not manual cockpit operations in PROD.

---

## 8. Connectivity Architecture

### 8.1 The BTP Connectivity Stack

SAP BTP connectivity to on-premise and private backend systems is built on three complementary services:

```
╔══════════════════════════════════════════════════════════════════╗
║                  BTP CONNECTIVITY STACK                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  1. DESTINATION SERVICE                                          ║
║     • Repository of named connection definitions                 ║
║     • Stores: URL, proxy type, authentication method, certs      ║
║     • Consumed by: all BTP applications                          ║
║                                                                  ║
║  2. CONNECTIVITY SERVICE                                         ║
║     • Platform-level SOCKS5 / HTTP proxy for on-premise traffic  ║
║     • Routes through Cloud Connector tunnel                      ║
║     • Required service binding for CF apps using on-premise dests║
║                                                                  ║
║  3. SAP CLOUD CONNECTOR (SCC)                                    ║
║     • On-premise agent running in customer network               ║
║     • Creates outbound TLS tunnel (port 443) to BTP              ║
║     • No inbound firewall rules required                         ║
║     • Maintains explicit allow-list of exposed resources         ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### 8.2 SAP Cloud Connector — Architecture and Deployment

The SAP Cloud Connector (SCC) is the on-premise agent that creates the secure tunnel between the customer's private network and SAP BTP. It is the cornerstone of the hybrid connectivity model.

#### Technical Specifications

| Attribute | Specification |
|---|---|
| **Deployment** | Windows Server 2019/2022/2025 or Linux (RHEL, SLES, Ubuntu) |
| **Java Runtime** | Bundled — no separate JVM installation required |
| **Admin Interface** | HTTPS on port **8443** — accessible from internal network only |
| **BTP Tunnel** | Outbound TLS on **port 443** — single persistent HTTPS connection to BTP |
| **RFC Proxy Port** | Port **20001** — used by Connectivity Proxy in Kyma for RFC protocol |
| **Supported Protocols** | HTTP, HTTPS, RFC/RfC, LDAP, TCP |
| **HA Model** | Master + Shadow (Hot Standby) — identical hardware/software |
| **HA Failover** | Automatic — Shadow promotes to Master within seconds on Master failure |
| **Installation footprint** | ~500 MB disk; 4 GB RAM minimum recommended for production |

#### Cloud Connector Network Requirements

```
Customer On-Premise Network                     SAP BTP (Cloud)
┌──────────────────────┐                    ┌───────────────────┐
│  SAP Cloud Connector │                    │  Connectivity     │
│  (Master + Shadow)   │─── Port 443 ──────>│  Service          │
│                      │  Outbound TLS only │                   │
│  Admin UI: Port 8443 │                    │  (SAP-managed)    │
│  (internal access)   │                    │                   │
│                      │                    │                   │
│  Connected to:       │                    │                   │
│  S/4HANA / ECC       │                    │                   │
│  (via allow-list)    │                    │                   │
└──────────────────────┘                    └───────────────────┘

Firewall rules required:
  OUTBOUND: SCC server → *.hana.ondemand.com : 443 (HTTPS/TLS)
  INBOUND:  None required from internet
```

#### Resource Allow-list Model

SCC exposes on-premise resources to BTP via an **explicit allow-list** — only what is explicitly listed is accessible.

| Resource Type | Allow-list Entry Format | Example |
|---|---|---|
| HTTP/HTTPS ICF path | Virtual host: path | `s4h-virtual:443 → s4h-internal:44300/sap/opu/odata/` |
| RFC Function Module | RFC destination: FM pattern | `RFC_VIRTUAL → SID:SYSNR / BAPI_PO_GETLIST` |
| LDAP | Virtual host | `ldap-virtual:389 → ldap.corp.com:389` |

> **Security principle**: Start with the minimum required paths. Never expose `/sap/` or `*` wildcard entries in production. Define the narrowest possible path and function module allow-list.

#### Cloud Connector HA Deployment Pattern

```
Customer DMZ / Data Centre
┌─────────────────────────────────────────┐
│                                         │
│  ┌─────────────────┐                    │
│  │  SCC MASTER     │◄── Admin: 8443    │
│  │  (Active)       │                    │
│  │                 │──── Port 443 ─────►│──── SAP BTP
│  └────────┬────────┘                    │
│           │ Master/Shadow sync          │
│  ┌────────▼────────┐                    │
│  │  SCC SHADOW     │◄── Admin: 8443    │
│  │  (Standby)      │                    │
│  │                 │──── Port 443 ─────►│──── SAP BTP (standby tunnel)
│  └─────────────────┘                    │
│                                         │
└─────────────────────────────────────────┘

Requirements:
- Same SCC version on both Master and Shadow
- No proxy server between Master and Shadow nodes
- Both nodes connected to same SAP BTP subaccount
- Separate VMs / hosts — not containers (HA requires process-level isolation)
```

### 8.3 Destination Service

The Destination Service is the central configuration repository for all outbound connections from BTP applications.

#### Key Destination Properties

| Property | Values | Purpose |
|---|---|---|
| `ProxyType` | `Internet`, `OnPremise`, `PrivateLink` | Routing method: public internet, Cloud Connector tunnel, or Private Link |
| `Authentication` | `NoAuthentication`, `BasicAuthentication`, `OAuth2ClientCredentials`, `OAuth2JWTBearer`, `PrincipalPropagation`, `SAMLAssertion`, `ClientCertificateAuthentication` | How BTP authenticates to the backend |
| `Type` | `HTTP`, `RFC`, `LDAP`, `MAIL`, `TCP` | Protocol handler |
| `URL` | Target URL | For OnPremise: virtual host defined in SCC |

#### Destination Hierarchy

Destinations can be defined at two levels:

```
Global Account Level Destinations:
  └── Accessible to all subaccounts (use for shared backend systems)

Subaccount Level Destinations:
  └── Scoped to the subaccount (use for environment-specific targets: DEV/TEST/PROD)
```

> **Best Practice**: Define separate destination definitions per landscape (DEV/TEST/PROD) at the subaccount level. Never share a PROD destination in a TEST subaccount. Use CI/CD pipeline to promote destination configurations as code.

### 8.4 Connectivity Patterns Summary

| Scenario | Pattern | Services Used |
|---|---|---|
| BTP App → SAP on-premise HTTP/OData | Cloud Connector + Connectivity + Destination | SCC, Connectivity Service, Destination Service |
| BTP App → SAP on-premise RFC | Cloud Connector RFC tunnel | SCC (RFC), JCo library in application |
| BTP App → Internet/cloud API | Direct internet, no SCC | Destination Service (ProxyType=Internet) |
| BTP App → Private cloud service (AWS/Azure) | SAP Private Link Service | Private Link Service, Destination Service (ProxyType=PrivateLink) |
| BTP App → S/4HANA, with user context | Principal Propagation via SCC | SCC (PP enabled), Destination, Connectivity |

---

## 9. Network Architecture and Hyperscaler Topology

### 9.1 BTP on Hyperscaler — Shared Responsibility Model

Understanding the **shared responsibility model** between SAP and the hyperscaler is critical for network architecture decisions.

```
┌──────────────────────────────────────────────────────────────────┐
│                    CUSTOMER RESPONSIBILITY                        │
│  Application code, custom security policies, data classification │
│  Network connectivity from customer side (SCC, Direct Connect)   │
├──────────────────────────────────────────────────────────────────┤
│                        SAP RESPONSIBILITY                         │
│  BTP platform services, runtime environments, service catalog    │
│  Platform patching, HA of platform services, audit log service   │
│  SAP Cloud Connector tunnel endpoint (connectivity service)      │
├──────────────────────────────────────────────────────────────────┤
│                    HYPERSCALER RESPONSIBILITY                     │
│  Underlying compute, network fabric, physical security           │
│  VPC/VNet infrastructure, DDoS protection, storage encryption    │
└──────────────────────────────────────────────────────────────────┘
```

### 9.2 BTP Network Topology — Internal

Within a BTP subaccount, network isolation is provided by the runtime environment's native model:

| Environment | Network Isolation Model |
|---|---|
| **Cloud Foundry** | CF Org → Spaces — apps in the same space share a network segment; inter-space communication requires service bindings or explicit routes |
| **Kyma** | Kubernetes namespaces with Istio service mesh — network policies, mTLS between services, Istio Ingress Gateway as the single entry point |
| **ABAP Environment** | SAP-managed tenant isolation — customer has no direct network configuration access |

### 9.3 Region and Data Residency

Subaccount region selection determines where customer data resides. This is **immutable** post-subaccount creation.

```
Region Selection Decision Framework:
                                          
  Data sovereignty requirement?           
  ├── AU/NZ data must stay in Australia → ap10 (AWS Sydney)
  ├── EU GDPR (EU data only) → eu10 (AWS Frankfurt) or eu20 (Azure Netherlands)
  ├── US data residency → us10 (AWS Virginia) or us20 (Azure Washington)
  └── Singapore/ASEAN → ap21 (Azure Singapore)
                                          
  Hyperscaler alignment (where other workloads run)?
  ├── AWS-primary customer → Prefer ap10 / eu10 / us10 regions
  └── Azure-primary customer → Prefer ap21 / eu20 / us20 regions
                                          
  Private Link requirement?
  └── Only available in specific regions — verify SAP Discovery Center
      for Private Link service availability in chosen region
```

---

## 10. Public Cloud to Private Cloud Connectivity

### 10.1 Overview and Connectivity Patterns

This section addresses the critical architecture pattern for connecting **SAP BTP (running on a public cloud hyperscaler)** to workloads and systems running in the **customer's private cloud environments** — whether that is a customer-managed VPC/VNet on the same hyperscaler, or on-premise networks connected via dedicated circuits.

The selection of the right pattern depends on:
1. **Where is the target system?** (on-premise, customer VPC/VNet, SAP RISE managed VPC)
2. **What is the traffic direction and security requirement?**
3. **Which hyperscaler hosts both BTP and the target workload?**
4. **Is internet traversal acceptable, or must traffic remain on private backbones?**

### 10.2 Connectivity Pattern Decision Framework

```
                    START: Where is the target system?
                                │
           ┌────────────────────┼────────────────────┐
           ▼                    ▼                    ▼
    On-Premise            Customer VPC/VNet     SAP RISE Private
    SAP System            (AWS or Azure)        Edition (Managed VPC)
           │                    │                    │
           ▼                    ▼                    ▼
   SAP Cloud            SAP Private Link        AWS Transit Gateway
   Connector            Service                 / Direct Connect
   (standard)           (preferred for         Gateway / VPN
                         private, no-internet)
```

### 10.3 SAP BTP Private Link Service

#### What is SAP BTP Private Link Service?

SAP BTP Private Link Service enables applications running on BTP (CF or Kyma) to connect to customer-hosted or partner-hosted services within a private VPC/VNet **without traversing the public internet**. It leverages the hyperscaler's native private service access infrastructure.

#### Architecture on AWS

```
SAP BTP (SAP-managed AWS VPC — ap10 / Sydney)
┌────────────────────────────────────────────────────┐
│  BTP Application (CF or Kyma)                      │
│       │                                            │
│       ▼                                            │
│  SAP Private Link Service                          │
│       │                                            │
│       │ Interface VPC Endpoint                     │
│       │ (Private IP in SAP-managed VPC)            │
└───────┼────────────────────────────────────────────┘
        │ AWS PrivateLink (AWS backbone — no internet)
┌───────┼────────────────────────────────────────────┐
│       │ VPC Endpoint Service                       │
│       │ (Network Load Balancer in customer VPC)    │
│       ▼                                            │
│  Customer Service / API / Database                 │
│  (Private IP — customer-managed AWS VPC)           │
│                                                    │
│  Customer AWS Account                              │
└────────────────────────────────────────────────────┘

Key properties:
✓ All traffic stays on AWS backbone — never traverses internet
✓ No VPC peering required — no routing overlap risk
✓ Unidirectional: BTP initiates; customer service receives
✓ Customer must accept the VPC Endpoint Service connection request
✓ DNS resolution required: private DNS zone maps service name to private IP
```

#### Architecture on Azure

```
SAP BTP (SAP-managed Azure VNet — eu20 / Netherlands or ap21 / Singapore)
┌────────────────────────────────────────────────────┐
│  BTP Application (CF or Kyma)                      │
│       │                                            │
│       ▼                                            │
│  SAP Private Link Service                          │
│       │                                            │
│       │ Azure Private Endpoint                     │
│       │ (Private IP in SAP-managed VNet)           │
└───────┼────────────────────────────────────────────┘
        │ Azure Private Link (Microsoft backbone — no internet)
┌───────┼────────────────────────────────────────────┐
│       │ Azure Private Link Service                 │
│       │ (Standard Load Balancer in customer VNet)  │
│       ▼                                            │
│  Customer Service / API / Database                 │
│  (Private IP — customer-managed Azure VNet)        │
│                                                    │
│  Customer Azure Subscription                       │
└────────────────────────────────────────────────────┘

Key properties:
✓ Traffic stays on Microsoft backbone
✓ Customer approves Private Endpoint connection request
✓ DNS: Azure Private DNS Zone required for name resolution
✓ Supports NVA (Network Virtual Appliance) for additional traffic inspection
```

#### Private Link Use Cases

| Use Case | Description |
|---|---|
| **Private REST API access** | BTP Integration Suite calling a customer-hosted REST API in VPC without public endpoint |
| **SAP HANA Cloud private access** | Connecting a BTP application to SAP HANA Cloud or customer database without public IP |
| **SAP S/4HANA Private Edition** | BTP extensions connecting to S/4HANA Private Edition hosted in customer AWS/Azure VPC |
| **Regulated workloads** | Financial services, healthcare — mandatory private connectivity with no internet routing |
| **AI/ML inference endpoints** | BTP AI workloads calling private ML model endpoints in customer VPC |

#### Private Link Implementation Checklist

- [ ] Identify the target service in customer AWS/Azure environment
- [ ] Customer creates VPC Endpoint Service (AWS) or Private Link Service (Azure) fronting the target
- [ ] BTP Private Link Service instance created in BTP subaccount
- [ ] Connection request from BTP Private Endpoint approved by customer AWS/Azure administrator
- [ ] Private DNS zone configured in customer VPC/VNet to resolve service hostname to private IP
- [ ] BTP Destination Service configured with `ProxyType = PrivateLink` pointing to the service hostname
- [ ] Application tested — validate no traffic leaves hyperscaler private network

### 10.4 AWS Direct Connect — On-Premise to Private Cloud Connectivity

AWS Direct Connect provides **dedicated physical connectivity** between a customer's on-premise network and AWS. When SAP BTP runs on AWS (e.g., `ap10` Sydney), Direct Connect enables private, non-internet end-to-end flows from on-premise through the customer AWS environment and then to BTP.

#### Architecture

```
On-Premise Data Centre / Customer Office
┌────────────────────────────────────────┐
│  Customer Router / WAN Edge            │
└────────────────────┬───────────────────┘
                     │ Dedicated Fibre
                     │ 1 / 10 / 100 Gbps
┌────────────────────▼───────────────────┐
│  AWS Direct Connect Location           │
│  (Equinix SY3, NextDC S1, etc.)        │
└────────────────────┬───────────────────┘
                     │ AWS Direct Connect circuit
┌────────────────────▼───────────────────┐
│  AWS Transit Gateway (Customer account)│
│       │ Private routing tables          │
│       ▼                                │
│  Customer VPC (private subnets)        │
│       │                                │
│       │ AWS PrivateLink / VPC Endpoint  │
│       ▼                                │
│  SAP BTP services or other targets     │
└────────────────────────────────────────┘

Note: SAP BTP itself (SAP-managed AWS VPC) does not support Direct Connect
peering directly. Direct Connect connects to the CUSTOMER's AWS account/VPC.
From there, SAP Private Link Service or Cloud Connector tunnel reaches BTP.
```

#### Direct Connect Variants

| Variant | Bandwidth | Use Case |
|---|---|---|
| **Dedicated Connection** | 1 Gbps, 10 Gbps, 100 Gbps | Large enterprise; requires colocation at DX location or partner handoff |
| **Hosted Connection** | 50 Mbps–10 Gbps | Ordered through AWS Partner network; flexible capacity |
| **Direct Connect Gateway** | N/A (logical) | Connects multiple VPCs across regions to a single DX connection |
| **Transit Gateway + DX** | N/A (logical) | Hub-and-spoke: one DX connection reaches all VPCs via TGW |

### 10.5 Azure ExpressRoute — On-Premise to Private Cloud Connectivity

Azure ExpressRoute is the Azure equivalent of AWS Direct Connect. For SAP BTP deployments on Azure (`eu20`, `ap21`), ExpressRoute provides the private backbone for on-premise to cloud traffic.

#### Architecture

```
On-Premise Data Centre
┌────────────────────────────────────────┐
│  Customer Router / WAN Edge            │
└────────────────────┬───────────────────┘
                     │ Dedicated circuit (via provider: Telstra, Optus, etc.)
┌────────────────────▼───────────────────┐
│  ExpressRoute Peering Location         │
│  (Equinix SY2, NextDC M1, etc.)        │
└────────────────────┬───────────────────┘
                     │ Microsoft backbone
┌────────────────────▼───────────────────┐
│  Azure (Microsoft network)             │
│  ┌─────────────────────────────────┐   │
│  │ Private Peering (VNet Access)   │   │
│  │  → Customer VNet (private IPs)  │   │
│  └─────────────────────────────────┘   │
│  ┌─────────────────────────────────┐   │
│  │ Microsoft Peering (Azure PaaS)  │   │
│  │  → Azure PaaS services          │   │
│  └─────────────────────────────────┘   │
│       │ Azure Private Endpoint          │
│       ▼                                │
│  SAP BTP (Azure) target service        │
└────────────────────────────────────────┘
```

#### ExpressRoute Tiers

| Tier | Bandwidth | Notes |
|---|---|---|
| **Standard** | 50 Mbps – 10 Gbps | Per-circuit; single region |
| **Premium** | 50 Mbps – 10 Gbps | Multi-region routing; global reach; add-on |
| **ExpressRoute Direct** | 10 Gbps or 100 Gbps | Direct port allocation at peering location; highest bandwidth |
| **ExpressRoute Global Reach** | Varies | Connect two on-premise networks through Azure backbone |

### 10.6 SAP RISE with SAP — Private Connectivity to Managed VPC

For customers running **SAP S/4HANA Private Edition (RISE with SAP)**, the S/4HANA system operates in an **SAP-managed AWS/Azure account**. Connecting BTP extensions to RISE requires specific connectivity patterns.

#### AWS RISE Connectivity Options

| Option | Description | Use Case |
|---|---|---|
| **AWS Transit Gateway (TGW) Peering** | Customer shares TGW resource with SAP's managed AWS account | Preferred for BTP extensions on same AWS region as RISE |
| **AWS VPN over TGW** | IPsec VPN from on-premise to TGW; on-premise reaches RISE VPC | When Direct Connect is not available |
| **Direct Connect Gateway** | DX connection from on-premise to RISE VPC via DX Gateway | On-premise to RISE, highest performance |

> **Note**: RISE connectivity is governed by SAP's RISE network integration guide. Connectivity architecture must be agreed with SAP during the RISE onboarding process. BTP and RISE may share the same AWS region, enabling private routing between SAP-managed VPCs.

### 10.7 Connectivity Pattern Comparison Summary

| Scenario | Pattern | Internet Traversal | Latency | Cost |
|---|---|---|---|---|
| BTP → On-premise SAP (ECC/S4H) | SAP Cloud Connector (TLS tunnel) | No (tunnel via TLS on 443) | Low-Medium | Low (SCC is included in BTP) |
| BTP → Customer private service in AWS VPC | SAP Private Link Service (AWS PrivateLink) | No (AWS backbone) | Very Low | Medium (Private Link Service fee) |
| BTP → Customer private service in Azure VNet | SAP Private Link Service (Azure Private Link) | No (Azure backbone) | Very Low | Medium |
| On-premise → AWS VPC | AWS Direct Connect | No (dedicated circuit) | Very Low | High (circuit + port fee) |
| On-premise → Azure VNet | Azure ExpressRoute | No (dedicated circuit) | Very Low | High (circuit + gateway fee) |
| BTP → RISE S/4HANA (AWS) | AWS Transit Gateway (RISE) | No (AWS backbone) | Very Low | Medium |
| BTP → Internet API | Destination Service (ProxyType=Internet) | Yes | Varies | Low |

---

## 11. SAP Integration Suite — Foundation Service

### 11.1 Role in the BTP Foundation

SAP Integration Suite is the **primary integration platform-as-a-service (iPaaS)** for the Intelligent Enterprise. As a BTP foundation service, it is typically one of the first services provisioned on BTP — it connects SAP cloud applications (S/4HANA Cloud, SuccessFactors) to on-premise systems and third-party SaaS platforms.

### 11.2 Core Capabilities Relevant to Foundation

| Capability | Purpose | Foundation Relevance |
|---|---|---|
| **Cloud Integration (CPI)** | Message-based integration; pre-built iFlows and adapters | Primary A2A and B2B integration; replaces SAP PI/PO |
| **API Management** | Full API lifecycle — design, publish, secure, monitor | API governance layer; expose BTP services securely to consumers |
| **Event Mesh** | Pub/sub messaging for event-driven architectures | Event-driven integrations; decoupled microservice communication |
| **Open Connectors** | 170+ pre-built connectors for third-party SaaS | Rapid integration with Salesforce, ServiceNow, Workday |
| **Integration Advisor** | ML-assisted B2B/EDI mapping | Accelerate EDI/B2B integrations |

### 11.3 Integration Suite Network Architecture

Integration Suite runs within BTP Cloud Foundry and uses the same connectivity infrastructure:

```
External System / Third-Party SaaS
          │ Internet (HTTPS/SFTP/AS2)
          ▼
    Integration Suite (CPI)          ← Runs in BTP CF environment
          │
          │ On-premise destinations → Cloud Connector → SAP ECC/S4H
          │ Private Link destinations → Private Link Service → Customer VPC
          │ Internet destinations → Direct HTTPS to cloud systems
          ▼
    SAP S/4HANA Cloud / SuccessFactors / etc.
```

### 11.4 API Management Architecture

```
API Consumer (Mobile App / Partner / Internal App)
          │
          ▼
    API Developer Portal   ← API discovery, documentation, subscription
          │
          ▼
    API Proxy (API Management Runtime)
          │ Policy enforcement: OAuth2, rate limiting, JWT validation, IP allowlist
          ▼
    Backend Service / iFlow / SAP OData API
```

---

## 12. ALM, Monitoring and Operations

### 12.1 SAP Cloud ALM

**SAP Cloud ALM** is SAP's cloud-native ALM solution for managing both the **implementation lifecycle** and the **operations** of SAP BTP and connected SAP applications. It is included in the BTPEA/CPEA commercial model.

#### Operations Capabilities

| Capability | Function | BTP Relevance |
|---|---|---|
| **Health Monitoring** | Unified health status of systems, services, and infrastructure | Monitors BTP services, ABAP environment, HANA Cloud, Cloud Connector |
| **Integration Monitoring** | Interface error rates, message volumes, latency | Monitors Integration Suite iFlow execution and error rates |
| **Job & Automation Monitoring** | ABAP/BTP background job health | Monitors scheduled jobs in ABAP Environment |
| **Business Process Monitoring** | End-to-end process KPIs | Cross-system process health across BTP and S/4HANA |
| **Real User Monitoring** | Synthetic and real-user transaction performance | Front-end application performance monitoring |
| **Operations Automation** | AI-assisted alert analysis and automated remediation | Integrates with SAP Build Process Automation for auto-remediation |

#### Cloud Connector Monitoring in Cloud ALM

| Metric | Alert Condition |
|---|---|
| Tunnel Availability | Tunnel disconnected / connection health degraded |
| Master/Shadow failover events | Shadow promoted to Master — requires investigation |
| Back-end system availability | Whitelisted system not responding through SCC |

### 12.2 SAP Alert Notification Service for SAP BTP

A dedicated BTP service for real-time operational alerts from BTP platform events and application-level events.

**Delivery Channels:**

| Channel | Notes |
|---|---|
| Email | Direct email to operations team or DL |
| Slack / Microsoft Teams | Webhook integration; preferred for DevOps teams |
| PagerDuty | On-call rotation integration |
| OpsGenie | Incident management integration |
| Generic Webhook | Any custom endpoint / SIEM integration |

**Alert Categories:**

| Category | Example Events |
|---|---|
| Platform events | Service instance creation failure, binding failure |
| Application events | CF app crash, health check failures |
| Integration Suite | iFlow message processing errors, adapter failures |
| Infrastructure | Subaccount quota approaching limit |
| Custom | Application-defined events via Alert Notification API |

### 12.3 BTP Operations Tooling Summary

| Tool | Purpose | Where to Access |
|---|---|---|
| **BTP Cockpit** | Primary GUI for account/service/entitlement management | `cockpit.btp.cloud.sap` |
| **BTP CLI (`btp`)** | Command-line automation and scripting | Install from SAP BTP tools page |
| **CF CLI** | Cloud Foundry app deployment and management | `cf push`, `cf logs`, `cf services` |
| **kubectl + BTP Operator** | Kyma workload management | Standard Kubernetes tooling |
| **SAP Cloud ALM** | Health monitoring, operations, ALM | SAP Cloud ALM tenant |
| **Alert Notification Service** | Proactive alerting | BTP service in subaccount |
| **SAP Audit Log Viewer** | Security audit log review | BTP Cockpit → Security |

---

## 13. Foundation Sizing and Commercials Guidance

### 13.1 Commercial Models

| Model | Best For | Key Characteristics |
|---|---|---|
| **BTPEA (BTP Enterprise Agreement)** | Large enterprise, multi-workload BTP program | Credit-based consumption; pre-committed credits; broadest service access; recommended for enterprise BTP journeys |
| **CPEA (Cloud Platform Enterprise Agreement)** | Established SAP customers expanding to BTP | Similar to BTPEA; covers broader SAP cloud portfolio |
| **Pay-As-You-Go (PAYG)** | PoC, exploration, short-term projects | Metered; no commitment; access to free tier services; higher unit cost |
| **Subscription** | Specific services with predictable usage | Fixed capacity; predictable cost; no flexibility on quota |

### 13.2 Foundation Services Sizing Recommendations

The following services should be provisioned as part of the BTP Foundation:

| Service | Plan | Sizing Guidance |
|---|---|---|
| SAP Cloud Identity Services (IAS) | Included with BTPEA | One tenant per Global Account; shared across all subaccounts |
| SAP Integration Suite | Standard or Premium | Standard for <5 integration scenarios; Premium for large-scale integration programs |
| SAP Cloud ALM | Included in SAP Enterprise Support | One tenant; connects to all BTP subaccounts |
| SAP Cloud Connector | Free (no BTP cost) | Minimum 2 instances (Master + Shadow) per data centre |
| SAP Alert Notification Service | Free tier (basic) | Upgrade to paid plan for higher event volumes |
| SAP Credential Store | Standard | Plan based on number of stored credentials |
| SAP Audit Log Service | Standard | Required in all productive subaccounts |
| SAP Private Link Service | Standard | Per service instance (per private endpoint needed) |

---

## 14. Implementation Roadmap

### 14.1 Phased Approach

The BTP Foundation is best delivered in phases to manage change and build internal capability.

```
PHASE 1: FOUNDATION CORE (Weeks 1–6)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ✓ Global Account provisioning and structure
  ✓ Directory and subaccount hierarchy (DEV/TEST/PROD)
  ✓ Entitlement allocation per subaccount
  ✓ SAP Cloud Identity Services (IAS/IPS) setup
  ✓ Corporate IdP federation (Entra ID → IAS)
  ✓ Role Collection design and group mapping
  ✓ BTP Cockpit access model and administrators confirmed

PHASE 2: CONNECTIVITY (Weeks 5–10)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ✓ SAP Cloud Connector deployment (Master + Shadow HA)
  ✓ Connectivity and Destination Service configuration
  ✓ On-premise resource allow-list configuration (per system)
  ✓ Principal propagation end-to-end test
  ✓ Private Link Service configuration (if applicable)
  ✓ Network firewall rules validated (outbound 443 from SCC servers)

PHASE 3: SECURITY HARDENING (Weeks 8–12)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ✓ Audit Log Service enabled in all productive subaccounts
  ✓ Log forwarding to SIEM configured and tested
  ✓ MFA enforced in IAS for productive users
  ✓ Credential Store configured; application secrets migrated
  ✓ Credential rotation schedule defined and automated
  ✓ Security review against Top 10 BTP Security Recommendations

PHASE 4: OPERATIONS (Weeks 10–14)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ✓ SAP Cloud ALM tenant connected to BTP subaccounts
  ✓ Health Monitoring dashboards configured
  ✓ Alert Notification Service configured with delivery channels
  ✓ Runbooks documented: SCC failover, credential rotation, incident response
  ✓ BTP operations handover to customer operations team

PHASE 5: FIRST WORKLOAD ONBOARDING (Weeks 12–18)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ✓ Integration Suite provisioned in SharedServices-PROD subaccount
  ✓ First integration scenario deployed (e.g., S/4HANA → 3rd party)
  ✓ API Management configured; first API proxy published
  ✓ Foundation validated end-to-end with live workload
```

---

## 15. Architecture Decision Register

| ID | Decision | Options Considered | Decision Made | Rationale |
|---|---|---|---|---|
| ADR-001 | Directory structure model | By BU / By Region / By Program | **By Business Unit** | Aligns to cost centre ownership and existing organizational structure |
| ADR-002 | DEV/TEST/PROD separation | Same subaccount / Separate subaccounts | **Separate subaccounts** | Blast radius isolation; prevents accidental PROD impact; different trust/security policies |
| ADR-003 | Corporate IdP integration | Direct per-subaccount SAML / Centralized via IAS | **Centralized via IAS** | Single trust point; MFA centrally managed; scalable as subaccounts grow |
| ADR-004 | Primary production region | ap10 (AWS Sydney) / ap21 (Azure Singapore) | **ap10 (AWS Sydney)** | Data residency in Australia; aligns to customer primary hyperscaler (AWS) |
| ADR-005 | Cloud Connector HA model | Single SCC / Master-Shadow pair | **Master-Shadow pair** | Production SLA requirement; automated failover without manual intervention |
| ADR-006 | Private cloud connectivity | VPC Peering / SAP Private Link / Cloud Connector | **SAP Private Link Service** for cloud; **Cloud Connector** for on-premise | Private Link is simpler, more secure, no routing overlap for cloud targets; SCC is the standard for on-premise |
| ADR-007 | Commercial model | PAYG / BTPEA / Subscription | **BTPEA** | Multi-workload program; credit flexibility; broadest service access |
| ADR-008 | Runtime environment (initial) | Cloud Foundry / Kyma / ABAP | **Cloud Foundry (primary)** with **Kyma** roadmap | CF lower operational complexity for initial workloads; Kyma introduced when containerised workloads arrive |

---

## 16. Glossary

| Term | Definition |
|---|---|
| **AMS** | Authorization Management Service — policy-based authorization service on BTP (successor to XSUAA scopes) |
| **BTPEA** | BTP Enterprise Agreement — credit-based commercial model for SAP BTP |
| **CAP** | SAP Cloud Application Programming Model — opinionated framework for building services on BTP |
| **CF** | Cloud Foundry — managed PaaS runtime environment within SAP BTP |
| **CPEA** | Cloud Platform Enterprise Agreement — predecessor to BTPEA; credit-based consumption model |
| **DX** | AWS Direct Connect — dedicated private circuit from on-premise to AWS |
| **ExpressRoute** | Azure ExpressRoute — dedicated private circuit from on-premise to Microsoft Azure |
| **IAS** | Identity Authentication Service — SAP Cloud Identity Services component for authentication and SSO |
| **IPS** | Identity Provisioning Service — SAP Cloud Identity Services component for user/group synchronization |
| **Kyma** | Kubernetes-based managed runtime environment within SAP BTP |
| **mTLS** | Mutual TLS — both client and server authenticate via X.509 certificates |
| **PAYG** | Pay-As-You-Go — metered, on-demand commercial model for BTP services |
| **Principal Propagation** | Pattern for forwarding authenticated BTP user identity to downstream on-premise systems |
| **Private Link** | SAP BTP Private Link Service — enables private connectivity to hyperscaler-hosted services without internet |
| **RAP** | ABAP RESTful Application Programming model — framework for building OData APIs on ABAP environment |
| **SCC** | SAP Cloud Connector — on-premise agent creating outbound TLS tunnel to SAP BTP |
| **SCIM** | System for Cross-domain Identity Management — protocol used by IPS for user provisioning |
| **SIEM** | Security Information and Event Management — system for log aggregation and security analysis |
| **TGW** | AWS Transit Gateway — centralized hub for connecting multiple VPCs and on-premise networks |
| **XSUAA** | Authorization and Trust Management Service — OAuth 2.0 authorization server within BTP subaccounts |

---

## 17. References

| Reference | URL |
|---|---|
| SAP BTP Help Portal | https://help.sap.com/docs/btp |
| SAP Discovery Center (Service Catalog) | https://discovery-center.cloud.sap |
| SAP Trust Center | https://www.sap.com/about/trust-center/security.html |
| SAP BTP Account Architecture Best Practices | https://community.sap.com/t5/technology-blog-posts-by-sap/sap-btp-account-architecture-best-practices |
| SAP Cloud Identity Services Documentation | https://help.sap.com/docs/cloud-identity-services |
| SAP Cloud Connector Documentation | https://help.sap.com/docs/connectivity/sap-btp-connectivity-cf/cloud-connector |
| SAP BTP Private Link Service | https://help.sap.com/docs/private-link |
| SAP Cloud ALM Documentation | https://support.sap.com/en/alm/sap-cloud-alm.html |
| SAP Alert Notification Service | https://help.sap.com/docs/alert-notification |
| SAP Integration Suite | https://help.sap.com/docs/sap-integration-suite |
| AWS Direct Connect Documentation | https://docs.aws.amazon.com/directconnect/ |
| AWS PrivateLink Documentation | https://docs.aws.amazon.com/vpc/latest/privatelink/ |
| AWS RISE Connectivity Guide | https://docs.aws.amazon.com/sap/latest/general/rise-connection-accounts.html |
| Azure ExpressRoute Documentation | https://learn.microsoft.com/en-us/azure/expressroute/ |
| Azure Private Link Documentation | https://learn.microsoft.com/en-us/azure/private-link/ |
| SAP BTP C5 Audit Report | https://www.sap.com/about/trust-center/certification-compliance/sap-btp-c5-2024-h1.html |
| SAP BTP SOC 2 Report | https://www.sap.com/about/trust-center/certification-compliance/sap-central-cloud-services-soc-2-audit-report-2025-h1.html |

---

*Document prepared by SAP BTP Technology Consulting — ANZ | Version 1.0 | August 2026*  
*This document is intended as a starting-point Solution Design. Architecture decisions should be validated against the specific customer context, data residency requirements, and SAP roadmap prior to implementation.*
