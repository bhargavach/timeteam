# SAP BTP Platform — Persona Access & Workflow Diagrams

> All diagrams are ADO Wiki compatible Mermaid. Paste each fenced block directly into an ADO Wiki page.
> Diagrams are intentionally kept simple — no protocol or security layer detail.

---

## Diagram Index

| # | Diagram | Type | Scenario |
|---|---|---|---|
| 1 | Persona to BTP Service Map | Flowchart (LR) | Who accesses what — overview |
| 2 | Developer — App Development Workflow | Sequence | Dev logs in, builds and deploys an app |
| 3 | Technical Consultant — Platform Service Configuration | Sequence | Configuring BTP services in a subaccount |
| 4 | Technical Consultant — Onboarding a Subaccount to SCC | Sequence | Connecting a BTP subaccount to Cloud Connector |
| 5 | Technical Consultant — IAS Authentication Setup | Sequence | Configuring IAS and Entra ID federation |
| 6 | Analyst — Monitoring and Audit Access | Sequence | Viewing audit logs and platform health |
| 7 | Product Owner — Read-Only Platform Visibility | Flowchart (TD) | Navigating BTP Cockpit for status and usage |
| 8 | New Developer Onboarding — From Access Request to First Dev Space | Flowchart (TD) | End-to-end access provisioning journey |

---

## Diagram 1 — Persona to BTP Service Map

> Who each persona interacts with on the BTP platform.

```mermaid
flowchart LR
    classDef persona  fill:#1565C0,color:#fff,stroke:#0D47A1,font-weight:bold
    classDef service  fill:#2E7D32,color:#fff,stroke:#1B5E20
    classDef platform fill:#E65100,color:#fff,stroke:#BF360C
    classDef external fill:#546E7A,color:#fff,stroke:#37474F

    DEV["🧑‍💻 Developer"]
    ANL["📊 Analyst"]
    PO["📋 Product Owner"]
    TC["🔧 Technical Consultant"]

    BTP["BTP Cockpit"]
    BAS["Business Application Studio"]
    CICD["CI/CD Service"]
    SA["Subaccounts"]
    SVC["Service Marketplace"]
    ALS["Audit Log Viewer"]
    ANS["Alert Notification"]
    MON["Monitoring Dashboard"]
    SCC["Cloud Connector Admin UI"]
    IAS["IAS Admin Console"]
    DST["Destination Service"]
    S4["S/4HANA TRBK"]

    DEV --> BTP
    DEV --> BAS
    DEV --> CICD
    DEV --> DST

    ANL --> BTP
    ANL --> ALS
    ANL --> ANS
    ANL --> MON

    PO --> BTP
    PO --> MON

    TC --> BTP
    TC --> SA
    TC --> SVC
    TC --> SCC
    TC --> IAS
    TC --> DST

    BAS --> S4
    DST --> S4

    class DEV,ANL,PO,TC persona
    class BAS,CICD,ALS,ANS,MON,DST service
    class BTP,SA,SVC platform
    class SCC,IAS,S4 external
```

---

## Diagram 2 — Developer: App Development Workflow

> A developer logs in via SSO, opens a dev space in BAS, builds and previews a Fiori app against S/4HANA, and deploys it.

```mermaid
sequenceDiagram
    autonumber
    actor Dev as Developer
    participant IAS as IAS / Entra ID
    participant Cockpit as BTP Cockpit
    participant BAS as Business Application Studio
    participant DS as Destination Service
    participant S4 as S/4HANA TRBK

    Dev->>IAS: Sign in (corporate SSO)
    IAS-->>Dev: Authenticated — redirect to BTP Cockpit

    Dev->>Cockpit: Navigate to Subaccount → Services → BAS
    Cockpit-->>Dev: BAS subscription open

    Dev->>BAS: Open Dev Space (Fiori / Full Stack)
    BAS-->>Dev: Dev Space ready

    Dev->>BAS: Create new Fiori project from template
    Dev->>BAS: Add OData service connection (S/4HANA)

    BAS->>DS: Resolve destination: TRBK-DEV
    DS-->>BAS: Backend URL + credentials returned

    BAS->>S4: Fetch OData metadata for live preview
    S4-->>BAS: Metadata received

    Dev->>BAS: Build and run app preview
    BAS-->>Dev: App renders in browser preview

    Dev->>BAS: Commit changes to Git (GitHub / Azure DevOps)
    BAS-->>Dev: Push successful

    Dev->>Cockpit: Trigger CI/CD pipeline — deploy to S/4HANA FES
    Cockpit-->>Dev: Deployment job submitted

    Note over Dev,S4: App is now live on S/4HANA Fiori Front-End Server
```

---

## Diagram 3 — Technical Consultant: Platform Service Configuration

> A consultant logs in, navigates to a subaccount, provisions a new BTP service, and configures it for use.

```mermaid
sequenceDiagram
    autonumber
    actor TC as Technical Consultant
    participant IAS as IAS / Entra ID
    participant Cockpit as BTP Cockpit
    participant SA as Subaccount
    participant Mkt as Service Marketplace
    participant Svc as Service Instance

    TC->>IAS: Sign in (corporate SSO)
    IAS-->>TC: Authenticated

    TC->>Cockpit: Open Global Account — ASB Bank NZ
    Cockpit-->>TC: Global Account overview

    TC->>Cockpit: Select target Subaccount (e.g. connectivity-shared-dev)
    Cockpit-->>TC: Subaccount dashboard

    TC->>Mkt: Browse Service Marketplace
    Mkt-->>TC: Available services listed

    TC->>Mkt: Select service — create new instance
    Mkt-->>TC: Instance creation form

    TC->>SA: Fill instance name and plan — submit
    SA-->>TC: Service instance provisioning started

    SA-->>TC: Instance status: Created ✓

    TC->>Svc: Open instance — go to Service Keys
    TC->>Svc: Create service key for application binding
    Svc-->>TC: Service key generated (credentials)

    TC->>Cockpit: Navigate to Destinations — add new destination
    TC->>Cockpit: Enter destination URL, auth details — save
    Cockpit-->>TC: Destination saved and validated ✓

    Note over TC,Svc: Service is provisioned and ready for application use
```

---

## Diagram 4 — Technical Consultant: Onboarding a Subaccount to Cloud Connector

> Connecting a BTP subaccount to an on-premise Cloud Connector instance so BTP services can reach S/4HANA TRBK.

```mermaid
sequenceDiagram
    autonumber
    actor TC as Technical Consultant
    participant Cockpit as BTP Cockpit
    participant SA as Subaccount (connectivity-shared-dev)
    participant SCC as Cloud Connector Admin UI
    participant S4 as S/4HANA TRBK

    TC->>Cockpit: Sign in — navigate to target subaccount
    Cockpit-->>TC: Subaccount dashboard open

    TC->>SA: Go to Connectivity → Cloud Connectors
    SA-->>TC: No Cloud Connectors paired yet

    Note over TC,SA: Collect subaccount ID and region from Cockpit

    TC->>SCC: Open SCC Admin UI (via Jump Host)
    SCC-->>TC: SCC login screen

    TC->>SCC: Sign in with SCC administrator account
    SCC-->>TC: SCC Dashboard

    TC->>SCC: Connector → Add Subaccount
    TC->>SCC: Enter Region, Subaccount ID, Display Name
    TC->>SCC: Enter P-User or technical credentials
    SCC-->>TC: Subaccount pairing initiated

    SCC-->>SA: Outbound tunnel established to BTP
    SA-->>TC: Cloud Connector appears as Connected ✓

    TC->>SCC: Go to Cloud To On-Premise → Access Control
    TC->>SCC: Add System Mapping — Virtual Host → Internal Host (S/4HANA)
    TC->>SCC: Add Resource Path — allowed URL paths

    SCC-->>TC: Access control rules saved

    TC->>Cockpit: Go to Destinations — create destination using SCC proxy
    Cockpit-->>TC: Destination created and connection test: OK ✓

    Note over TC,S4: BTP subaccount is now connected to S/4HANA TRBK via Cloud Connector
```

---

## Diagram 5 — Technical Consultant: Setting Up IAS for Authentication

> Configuring SAP IAS as the identity broker, connecting Entra ID as the corporate identity provider, and enabling SSO for a BTP subaccount.

```mermaid
sequenceDiagram
    autonumber
    actor TC as Technical Consultant
    participant IAS as IAS Admin Console
    participant Cockpit as BTP Cockpit
    participant SA as Subaccount Trust Config
    participant Entra as Microsoft Entra ID

    TC->>IAS: Sign in to IAS Admin Console
    IAS-->>TC: IAS Dashboard

    TC->>IAS: Applications & Resources → Applications → New Application
    TC->>IAS: Name application — link to BTP subaccount
    IAS-->>TC: Application created

    TC->>IAS: Authentication & Access → Conditional Authentication
    TC->>IAS: Set default authenticating IdP → Microsoft Entra ID
    IAS-->>TC: Default IdP configured

    TC->>IAS: Identity Providers → Corporate Identity Providers → Add
    TC->>IAS: Upload Entra ID SAML metadata XML
    IAS-->>TC: Entra ID registered as Corporate IdP ✓

    TC->>Entra: Register IAS as Enterprise Application (SAML)
    TC->>Entra: Upload IAS SAML metadata — configure attribute mapping
    Entra-->>TC: Enterprise App configured ✓

    TC->>IAS: Test SSO — initiate SP-initiated login
    IAS->>Entra: Redirect user for authentication
    Entra-->>IAS: Authenticated user returned
    IAS-->>TC: SSO test successful ✓

    TC->>Cockpit: Open BTP subaccount → Security → Trust Configuration
    TC->>SA: Add Custom IAS Tenant as trusted identity provider
    SA-->>TC: IAS tenant trusted ✓

    TC->>Cockpit: Assign Role Collections to IAS user groups
    Cockpit-->>TC: Role Collections mapped

    Note over TC,Entra: Users can now sign in to BTP using corporate Entra ID credentials via IAS
```

---

## Diagram 6 — Analyst: Monitoring and Audit Access

> An analyst logs in to review audit events, check platform health, and export logs for compliance review.

```mermaid
sequenceDiagram
    autonumber
    actor Ana as Analyst
    participant IAS as IAS / Entra ID
    participant Cockpit as BTP Cockpit
    participant ALS as Audit Log Viewer
    participant ANS as Alert Notification
    participant Mon as Monitoring Dashboard

    Ana->>IAS: Sign in (corporate SSO)
    IAS-->>Ana: Authenticated — BTP Cockpit access (read-only)

    Ana->>Cockpit: Open Subaccount — monitoring-shared-prd
    Cockpit-->>Ana: Subaccount overview

    Ana->>ALS: Open Audit Log Viewer
    ALS-->>Ana: Audit log viewer loaded

    Ana->>ALS: Set time range filter — last 7 days
    Ana->>ALS: Filter by category — Security Events
    ALS-->>Ana: Security event log displayed

    Ana->>ALS: Review flagged events
    Ana->>ALS: Download filtered log export (CSV)
    ALS-->>Ana: Export file downloaded

    Ana->>ANS: Open Alert Notification — view active alerts
    ANS-->>Ana: Alert list displayed

    Ana->>ANS: Review alert history — last 30 days
    ANS-->>Ana: Alert history loaded

    Ana->>Mon: Open Monitoring Dashboard
    Mon-->>Ana: Platform health overview displayed

    Note over Ana,Mon: Analyst has read-only access — no configuration changes possible
```

---

## Diagram 7 — Product Owner: Read-Only Platform Visibility

> How a Product Owner navigates BTP Cockpit to view platform status, service usage, and subaccount health.

```mermaid
flowchart TD
    classDef po      fill:#1565C0,color:#fff,stroke:#0D47A1,font-weight:bold
    classDef step    fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef view    fill:#E8F5E9,color:#1B5E20,stroke:#2E7D32
    classDef noact   fill:#FFF3E0,color:#E65100,stroke:#FB8C00,font-style:italic

    PO(["📋 Product Owner\nSigns in via Entra ID SSO"])

    COCKPIT["BTP Cockpit\nGlobal Account View"]
    DIRS["Browse Directories\nintegration / application / analytics / platform"]
    SA_LIST["View Subaccounts\nand their status"]
    SVC_LIST["View Service Instances\nin each subaccount"]
    USAGE["View Usage and Billing\n(BTPEA credit consumption)"]
    HEALTH["View Service Health\nand Availability"]
    ALERTS["View Active Alerts\nfrom Alert Notification"]
    REPORT["Export Usage Report\nfor stakeholder review"]
    NOTE["⚠ Read-only access\nNo create / edit / delete permitted"]

    PO --> COCKPIT
    COCKPIT --> DIRS
    DIRS --> SA_LIST
    SA_LIST --> SVC_LIST
    COCKPIT --> USAGE
    COCKPIT --> HEALTH
    COCKPIT --> ALERTS
    USAGE --> REPORT

    SVC_LIST -.-> NOTE
    HEALTH -.-> NOTE
    ALERTS -.-> NOTE

    class PO po
    class COCKPIT,DIRS,SA_LIST,SVC_LIST,USAGE,HEALTH,ALERTS,REPORT step
    class NOTE noact
```

---

## Diagram 8 — New Developer Onboarding: From Access Request to First Dev Space

> The end-to-end journey when a new developer joins and needs access to the BTP platform.

```mermaid
flowchart TD
    classDef actor   fill:#1565C0,color:#fff,stroke:#0D47A1,font-weight:bold
    classDef process fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef system  fill:#2E7D32,color:#fff,stroke:#1B5E20
    classDef approve fill:#E65100,color:#fff,stroke:#BF360C
    classDef done    fill:#1B5E20,color:#fff,stroke:#0D3B22,font-weight:bold

    DEV(["🧑‍💻 New Developer\nJoins the team"])

    REQ["Developer raises\nAccess Request in SailPoint"]
    APPROVE["Line Manager\nApproves Request"]
    PROV["SailPoint provisions\nIAS Group membership"]
    IPS["SAP IPS syncs\nRole Collections to BTP"]
    XSUAA["XSUAA assigns\nDeveloper Role Collection\nto target subaccounts"]
    NOTIFY["Developer receives\nWelcome email with BTP URL"]
    LOGIN["Developer signs in\nvia Entra ID SSO"]
    COCKPIT["BTP Cockpit access\ngrant confirmed"]
    BAS["Developer opens\nBusiness Application Studio"]
    DS["Developer opens\nor creates Dev Space"]
    READY(["✅ Developer is ready\nto build on BTP"])

    DEV --> REQ
    REQ --> APPROVE
    APPROVE --> PROV
    PROV --> IPS
    IPS --> XSUAA
    XSUAA --> NOTIFY
    NOTIFY --> LOGIN
    LOGIN --> COCKPIT
    COCKPIT --> BAS
    BAS --> DS
    DS --> READY

    class DEV actor
    class REQ,PROV,IPS,XSUAA,NOTIFY,LOGIN,COCKPIT,BAS,DS process
    class APPROVE approve
    class READY done
```

---

## Usage Notes for ADO Wiki

- Paste any ` ```mermaid ``` ` block directly into an ADO Wiki Markdown page
- Diagrams render automatically — no plugin required in Azure DevOps (Mermaid is natively supported in ADO Wiki)
- If a diagram appears blank, check for unsupported characters or missing `classDef` spacing
- Recommended page structure:
  - **Parent page**: Diagram 1 (Persona Map) — links to all child pages
  - **Child pages**: One per diagram (Diagrams 2–8)
- To widen diagrams in ADO Wiki, use `flowchart LR` instead of `flowchart TD`
