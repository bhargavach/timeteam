# SAP BTP — Simple Traffic Flow Diagrams

> Minimal access flow diagrams for ADO Wiki. Each diagram is intentionally short — 5 to 7 nodes maximum.

---

## Diagram 1 — Global Account Administrator Access Flow

```mermaid
flowchart LR
    classDef user   fill:#1565C0,color:#fff,stroke:#0D47A1
    classDef system fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef scope  fill:#2E7D32,color:#fff,stroke:#1B5E20

    GA_ADMIN(["Global Account Admin"])
    LOGIN["Sign in via Entra ID"]
    COCKPIT["BTP Cockpit"]
    GLOBAL["Global Account\nASB Bank NZ"]
    DIRS["Manage Directories"]
    SUBS["Manage Subaccounts"]
    ENTITLE["Manage Entitlements\n& Licencing"]
    MEMBERS["Manage Global Members\n& Role Collections"]

    GA_ADMIN --> LOGIN --> COCKPIT --> GLOBAL
    GLOBAL --> DIRS
    GLOBAL --> SUBS
    GLOBAL --> ENTITLE
    GLOBAL --> MEMBERS

    class GA_ADMIN user
    class LOGIN,COCKPIT system
    class DIRS,SUBS,ENTITLE,MEMBERS scope
```

---

## Diagram 2 — Subaccount Administrator Access Flow

```mermaid
flowchart LR
    classDef user   fill:#1565C0,color:#fff,stroke:#0D47A1
    classDef system fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef scope  fill:#E65100,color:#fff,stroke:#BF360C

    SA_ADMIN(["Subaccount Admin"])
    LOGIN["Sign in via Entra ID"]
    COCKPIT["BTP Cockpit"]
    SA["Target Subaccount"]
    SVCS["Services &\nSubscriptions"]
    USERS["Users &\nRole Collections"]
    DEST["Destinations"]
    CONN["Connectivity\n& Cloud Connector"]

    SA_ADMIN --> LOGIN --> COCKPIT --> SA
    SA --> SVCS
    SA --> USERS
    SA --> DEST
    SA --> CONN

    class SA_ADMIN user
    class LOGIN,COCKPIT system
    class SA,SVCS,USERS,DEST,CONN scope
```

---

## Diagram 3 — Elevated User Access Flow

```mermaid
flowchart LR
    classDef user    fill:#1565C0,color:#fff,stroke:#0D47A1
    classDef process fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef approve fill:#E65100,color:#fff,stroke:#BF360C
    classDef done    fill:#2E7D32,color:#fff,stroke:#1B5E20

    USER(["User"])
    REQ["Raise Access\nRequest"]
    APPR["Manager\nApproval"]
    GRANT["Elevated Access\nGranted via SailPoint"]
    TARGET["Access Target\nService or Admin Area"]
    REVOKE["Access Revoked\non Completion"]

    USER --> REQ --> APPR --> GRANT --> TARGET --> REVOKE

    class USER user
    class REQ,TARGET process
    class APPR approve
    class GRANT,REVOKE done
```

---

## Diagram 4 — Standard End User Access Flow

```mermaid
flowchart LR
    classDef user   fill:#1565C0,color:#fff,stroke:#0D47A1
    classDef system fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef limit  fill:#546E7A,color:#fff,stroke:#37474F

    END_USER(["End User"])
    LOGIN["Sign in via Entra ID"]
    COCKPIT["BTP Cockpit\n(Assigned Subaccounts Only)"]
    ASSIGNED["Assigned Services\n& Applications"]
    USE["Use Service"]
    NO_ADMIN["⛔ No Admin,\nConfig or Setup Access"]

    END_USER --> LOGIN --> COCKPIT --> ASSIGNED --> USE
    COCKPIT -.-> NO_ADMIN

    class END_USER user
    class LOGIN,COCKPIT system
    class ASSIGNED,USE system
    class NO_ADMIN limit
```

---

## Diagram 5 — Creating a Destination

```mermaid
flowchart LR
    classDef user   fill:#1565C0,color:#fff,stroke:#0D47A1
    classDef step   fill:#F5F5F5,color:#212121,stroke:#9E9E9E
    classDef done   fill:#2E7D32,color:#fff,stroke:#1B5E20

    ADMIN(["Subaccount Admin"])
    SA["Open Subaccount\nin BTP Cockpit"]
    NAV["Connectivity\n→ Destinations"]
    NEW["+ New Destination"]
    FILL["Enter Name · URL\nProxy Type · Auth Type"]
    TEST["Test Connection"]
    OK(["✅ Destination Ready"])

    ADMIN --> SA --> NAV --> NEW --> FILL --> TEST --> OK

    class ADMIN user
    class SA,NAV,NEW,FILL,TEST step
    class OK done
```

---

## Diagram 6 — Access Level Comparison (All Personas)

> At-a-glance view of what each access level can reach.

```mermaid
flowchart TD
    classDef ga    fill:#1565C0,color:#fff,stroke:#0D47A1
    classDef sa    fill:#6A1B9A,color:#fff,stroke:#4A148C
    classDef elev  fill:#E65100,color:#fff,stroke:#BF360C
    classDef std   fill:#2E7D32,color:#fff,stroke:#1B5E20
    classDef block fill:#B71C1C,color:#fff,stroke:#7F0000,font-style:italic

    GA(["Global Account Admin"])
    SA_A(["Subaccount Admin"])
    ELEV(["Elevated User"])
    STD(["Standard End User"])

    GA_SVC["Global Account\nDirectories · Subaccounts\nEntitlements · Members"]
    SA_SVC["Subaccount Only\nServices · Users\nDestinations · Connectivity"]
    ELEV_SVC["Specific Service or Area\nTime-Limited\nApproval Required"]
    STD_SVC["Assigned Services\nRead & Use Only"]
    BLOCKED["⛔ Platform Config\nAdmin Functions\nNot Accessible"]

    GA --> GA_SVC
    SA_A --> SA_SVC
    ELEV --> ELEV_SVC
    STD --> STD_SVC
    STD -.-> BLOCKED

    class GA ga
    class SA_A sa
    class ELEV elev
    class STD std
    class BLOCKED block
```

---
