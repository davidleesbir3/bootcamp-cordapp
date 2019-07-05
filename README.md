
# Bootcamp CorDapp

This project is the template we will use as a basis for developing a complete CorDapp 
during today's bootcamp. Our CorDapp will allow the issuance of tokens onto the ledger.

We'll develop the CorDapp using a test-driven approach. At each stage, you'll know your 
CorDapp is working once it passes both sets of tests defined in `src/test/java/java_bootcamp`.

## Set up

1. Download and install Oracle JDK 8 JVM (minimum supported version 8u131)

       https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

2. Download and install IntelliJ Community Edition (supported versions 2018.x and 2019.x)

       https://www.jetbrains.com/idea/

3. Install git.
4. Download the bootcamp project.

       git clone https://github.com/sbir3japan/bootcamp-cordapp.git
       
5. Open IntelliJ. From the splash screen, click `Open`.
6. Run the test in `src/test/java/java_bootcamp/ProjectImportedOKTest.java`. It should pass!

## Links to useful resources

This project contains example state, contract and flow implementations:

* `src/main/java/java_examples/ArtState`
* `src/main/java/java_examples/ArtContract`
* `src/main/java/java_examples/ArtTransferFlowInitiator`
* `src/main/java/java_examples/ArtTransferFlowResponder`

There are also several web resources that you will likely find useful for this
bootcamp:

* Key Concepts docs (`docs.corda.net/key-concepts.html`)
* API docs (`docs.corda.net/api-index.html`)
* Cheat sheet (`docs.corda.net/cheat-sheet.html`)
* Sample CorDapps (`www.corda.net/samples`)
* Stack Overflow (`www.stackoverflow.com/questions/tagged/corda`)

## What we'll be building

Our CorDapp will have three parts:

### The TokenState

States define shared facts on the ledger. Our state, TokenState, will define a
token. It will have the following structure:

    -------------------
    |                 |
    |   TokenState    |
    |                 |
    |   - issuer      |
    |   - owner       |
    |   - amount      |
    |                 |
    -------------------

### The TokenContract

Contracts govern how states evolve over time. Our contract, TokenContract,
will define how TokenStates evolve. It will only allow the following type of
TokenState transaction:

    -------------------------------------------------------------------------------------
    |                                                                                   |
    |    - - - - - - - - - -                                     -------------------    |
    |                                              ▲             |                 |    |
    |    |                 |                       | -►          |   TokenState    |    |
    |            NO             -------------------     -►       |                 |    |
    |    |                 |    |      Issue command       -►    |   - issuer      |    |
    |          INPUTS           |     signed by issuer     -►    |   - owner       |    |
    |    |                 |    -------------------     -►       |   - amount > 0  |    |
    |                                              | -►          |                 |    |
    |    - - - - - - - - - -                       ▼             -------------------    |
    |                                                                                   |
    -------------------------------------------------------------------------------------

              No inputs             One issue command,                One output,
                                 issuer is a required signer       amount is positive

To do so, TokenContract will impose the following constraints on transactions
involving TokenStates:

* The transaction has no input states
* The transaction has one output state
* The transaction has one command
* The output state is a TokenState
* The output state has a positive amount
* The command is an Issue command
* The command lists the TokenState's issuer as a required signer

### The TokenIssueFlow

Flows automate the process of updating the ledger. Our flow, TokenIssueFlow, will
automate the following steps:

            Issuer                  Owner                  Notary
              |                       |                       |
       Chooses a notary
              |                       |                       |
        Starts building
         a transaction                |                       |
              |
        Adds the output               |                       |
          TokenState
              |                       |                       |
           Adds the
         Issue command                |                       |
              |
         Verifies the                 |                       |
          transaction
              |                       |                       |
          Signs the
         transaction                  |                       |
              |
              |----------------------------------------------►|
              |                       |                       |
                                                         Notarises the
              |                       |                   transaction
                                                              |
              |◀----------------------------------------------|
              |                       |                       |
         Records the
         transaction                  |                       |
              |
              |----------------------►|                       |
                                      |
              |                  Records the                  |
                                 transaction
              |                       |                       |
              ▼                       ▼                       ▼

## Running our CorDapp

Normally, you'd interact with a CorDapp via a client or webserver. So we can
focus on our CorDapp, we'll be running it via the node shell instead.

Once you've finished the CorDapp's code, run it with the following steps:

* Build a test network of nodes by opening a terminal window at the root of
  your project and running the following command:

    * Windows:   `gradlew.bat deployNodesJava`
    * macOS:     `./gradlew deployNodesJava`

* Start the nodes by running the following command:

    * Windows:   `build\nodes\runnodes.bat`
    * macOS:     `build/nodes/runnodes`

* Open the nodes are started, go to the terminal of Party A (not the notary!)
  and run the following command to issue 99 tokens to Party B:

    `flow start java_bootcamp.TokenIssueFlow owner: "O=PartyB,L=New York,C=US", amount: 99`

* You can now see the tokens in the vaults of Party A and Party B (but not 
  Party C!) by running the following command in their respective terminals:

    `run vaultQuery contractStateType: java_bootcamp.TokenState`

## Updating for offline use

* Run the `gatherDependencies` Gradle task from the root of the project to 
  gather all the CorDapp's dependencies in `lib/dependencies`
* Update `gradle/wrapper/gradle-wrapper.properties` to point to a local Gradle 
  distribution (e.g. 
  `distributionUrl=gradle-4.4.1-all.zip`)
* In `build.gradle`, under both `repositories` blocks, comment out any 
  repositories other than `flatDir { ... }`
