package java_bootcamp;

import net.corda.core.contracts.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {
    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getCommands().size() != 1) {
            throw new IllegalArgumentException("Transaction must have exactly one command");
        }

        Command command =  tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue();

        if (commandType instanceof Register) {
            // "Shape" constraints
            if (tx.getInputs().size() != 0)
                throw new IllegalArgumentException("Registration transaction must have no input");
            else if (tx.getOutputs().size() != 1)
                throw new IllegalArgumentException("Registration transaction must have exactly one output");

            // content constraints
            ContractState outputState = tx.getOutput(0);
            if (!(outputState instanceof HouseState))
                throw new IllegalArgumentException("Output must be a HouseState");

            HouseState houseState = (HouseState) outputState;
            if (houseState.getAddress().length() <= 3)
                throw new IllegalArgumentException("House address must be longer than 3 characters");
            if (houseState.getOwner().getName().getCountry() == "South Africa")
                throw new IllegalArgumentException(("Not allowed to register for South African users"));

            // Required signer constraints
            Party owner = houseState.getOwner();
            if (!requiredSigners.contains(owner.getOwningKey()))
                throw new IllegalArgumentException("House owner must sign registration");
        } else if (commandType instanceof Transfer) {
            // "Shape" constraints
            if (tx.getInputs().size() != 1)
                throw new IllegalArgumentException("Registration transaction must have exactly one input");
            else if (tx.getOutputs().size() != 1)
                throw new IllegalArgumentException("Registration transaction must have exactly one output");

            // content constraints
            ContractState inputState = tx.getInput(0);
            ContractState outputState = tx.getOutput(0);

            if (!(inputState instanceof HouseState))
                throw new IllegalArgumentException("Input must be HouseState");
            if (!(outputState instanceof HouseState))
                throw new IllegalArgumentException("Output must be HouseState");

            HouseState inputHouse = (HouseState) inputState;
            HouseState outputHouse = (HouseState) outputState;
            if (inputHouse.getAddress() == outputHouse.getAddress())
                throw new IllegalArgumentException("In a transfer, the address cannot change");
            if (inputHouse.getOwner() == outputHouse.getOwner())
                throw new IllegalArgumentException("In a transfer, the owner must change");

            // Required signer constraints
            Party inputOwner = inputHouse.getOwner();
            Party outputOwner = outputHouse.getOwner();
            if (!requiredSigners.contains(inputOwner.getOwningKey()))
                throw new IllegalArgumentException("Current owner must sign transfer");
            if (!requiredSigners.contains(outputOwner.getOwningKey()))
                throw new IllegalArgumentException("New owner must sign transfer");

        } else {
            throw new IllegalArgumentException("Command type not recognized");
        }

    }

    public static class Register implements CommandData {}
    public static class Transfer implements CommandData {}
}
