package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContainerState implements ContractState {
    private int width;
    private int height;
    private int depth;
    private String content;
    private Party owner;
    private Party carrier;

    public ContainerState(int width, int height, int depth, String content, Party owner, Party carrier) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.content = content;
        this.owner = owner;
        this.carrier = carrier;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public String getContent() {
        return content;
    }

    public Party getOwner() {
        return owner;
    }

    public Party getCarrier() {
        return carrier;
    }

    public static void main(String[] args) {
        Party importers = null;
        Party carriers = null;

        ContainerState state = new ContainerState(
                2,
                2,
                2,
                "My Contents",
                importers,
                carriers);
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner);
    }
}
