import java.util.LinkedList;

public class Process {
    private String name;

    private LinkedList<Integer> maxResProcess = new LinkedList<Integer>();

    private LinkedList<Integer> requestResProcess = new LinkedList<Integer>();

    private LinkedList<Integer> allocatedResProcess = new LinkedList<Integer>();

    public LinkedList<Integer> getMaxResProcess() {
        return maxResProcess;
    }

    public void setMaxResProcess(LinkedList<Integer> maxResProcess) {
        this.maxResProcess = maxResProcess;
    }

    public LinkedList<Integer> getRequestResProcess() {
        return requestResProcess;
    }

    public void setRequestResProcess(LinkedList<Integer> requestResProcess) {
        this.requestResProcess = requestResProcess;
    }

    public LinkedList<Integer> getAllocatedResProcess() {
        return allocatedResProcess;
    }

    public void setAllocatedResProcess(LinkedList<Integer> allocatedResProcess) {
        this.allocatedResProcess = allocatedResProcess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
