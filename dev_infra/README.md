## Development Infrastructure

The infrastructure contains the following:
* [Open ID Connect Mock Server](https://github.com/xdev-software/oidc-server-mock) - for login in
  * Available at http://localhost:4011

### Setup
* Requires Docker

#### OIDC
* Create ``oidc-user-config.json`` from [``oidc-user-config.json.template``](./oidc-user-config.json.template)
  * File should not be tracked in Git
  * Fill in your login details like mail, name, password


### Usage
Note: Commands are all executed inside a shell/CMD in the current folder. ([Tip for windows users](https://stackoverflow.com/a/40146208))

| Use case | What to do? |
| --- | --- |
| Starting the infrastructure | ``docker compose up`` |
| Stopping (and removing) the infrastructure | ``docker compose down`` |
| (Re)Building the infrastructure<br/>e.g. after changes to the Dockerfiles | ``docker compose build --pull`` |

See also ``docker compose --help``

### Additional notes
⚠ The containers don't automatically restart after a PC restart!

⚠ After a PC restart the infrastructure is still present but it's stopped.<br/>
In this case you have 2 options:
* start the existing infrastructure again (``docker compose up``) or
* do a clean start by first removing (``docker compose down``) and then starting the infrastructure
