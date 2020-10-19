import java.util.LinkedList;
import java.util.Scanner;

public class BankerAlgorithm {
    //资源类别数
    private Integer resNum = 0;

    //进程队列
    private LinkedList<Process> processRes = new LinkedList<Process>();

    //安全序列
    private LinkedList<Process> security = new LinkedList<Process>();

    //CPU资源可用值序列
    private LinkedList<LinkedList<Integer>> finalCPURes = new LinkedList<LinkedList<Integer>>();

    //CPU初始化资源可用值
    private LinkedList<Integer> cpuRes = new LinkedList<Integer>();

    public LinkedList<LinkedList<Integer>> getFinalCPURes() {
        return finalCPURes;
    }

    public void setFinalCPURes(LinkedList<LinkedList<Integer>> finalCPURes) {
        this.finalCPURes = finalCPURes;
    }

    public LinkedList<Process> getSecurity() {
        return security;
    }

    public void setSecurity(LinkedList<Process> security) {
        this.security = security;
    }

    public LinkedList<Process> getProcessRes() {
        return processRes;
    }

    public void setProcessRes(LinkedList<Process> processRes) {
        this.processRes = processRes;
    }

    public Integer getResNum() {
        return resNum;
    }

    public void setResNum(Integer resNum) {
        this.resNum = resNum;
    }

    public LinkedList<Integer> getCpuRes() {
        return cpuRes;
    }

    public void setCpuRes(LinkedList<Integer> cpuRes) {
        this.cpuRes = cpuRes;
    }

    //查询是否存在相同名称的进程√
    public boolean haveSameName(String name) {
        int index = -1;
        if (processRes.size() != 0) {
            for (int i = 0; i < processRes.size(); i++) {
                if (processRes.get(i).getName().equals(name)) {
                    index = i;
                }
            }
        } else {
            return true;
        }
        if (index < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean confirmCreate() {
        if (security.size() > 0) {
            Scanner input = new Scanner(System.in);
            System.out.println("已存在安全序列，再创建进程会安全序列会被删除，确定要创建吗？（输入y以继续，随意输入以取消）");
            String inputString = input.nextLine();
            if (inputString.equals("y")) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean create() {
        Process process = new Process();
        LinkedList<Integer> max = new LinkedList<Integer>();
        Scanner input = new Scanner(System.in);
        System.out.println("请输入进程名(一个字符串)【请勿输入相同的进程名】：");
        String name = input.nextLine();
        while (name.length() == 0) {
            System.out.println("进程名不能为空，请重新输入：");
            name = input.nextLine();
        }
        while (!haveSameName(name)) {
            System.out.println("该进程名已存在，请重新输入：");
            name = input.nextLine();
        }
        process.setName(name);
        int a = 0;
        int b = 65;
        while (a < resNum) {
            System.out.println("请输入进程" + name + "对" + (char) b + "类资源的最大需求值(大于等于0的整数)：");
            try {
                int maxNum = Integer.parseInt(input.nextLine().trim());
                while (maxNum < 0) {
                    System.out.println("输入不合法，请重新输入进程" + name + "对" + (char) b + "类资源的最大需求值(大于等于0的整数)：");
                    maxNum = Integer.parseInt(input.nextLine().trim());
                }
                max.add(maxNum);
                a++;
                b++;
            } catch (Exception e) {
                System.out.println("输入不合法，请重新输入");
            }
        }
        int add = 0;
        for (int u = 0; u < resNum; u++) {
            add += max.get(u);
        }
        if (add == 0) {
            System.out.println("各类资源的最大需求值不能均为0,创建进程失败");
            return false;
        }
        process.setMaxResProcess(max);
        processRes.offer(process);
        security.clear();
        return true;
    }

    public boolean request() {
        boolean isFull = true;
        for (int r = 0; r < processRes.size(); r++) {
            if (processRes.get(r).getRequestResProcess().size() == 0) {
                isFull = false;
            }
        }
        if (isFull) {
            return true;
        }
        //判断是否需要输入申请资源值↑
        int k = 0;
        int t = 0;
        int j = 0;
        boolean u = true;
        while (k < processRes.size() && u) {
            while (processRes.get(k).getRequestResProcess().size() == 0 && t == 0) {
                j = k;
                t++;
                u = false;
            }
            k++;
        }
        //↑计算循环起点索引值
        Scanner input = new Scanner(System.in);
        int b = 65;
        while (j < processRes.size()) {
            int a = 0;
            LinkedList<Integer> request = new LinkedList<Integer>();
            LinkedList<Integer> allocated = new LinkedList<Integer>();
            while (a < resNum) {
                System.out.println("请输入进程" + processRes.get(j).getName() + "对" + (char) b + "类资源的资源申请值(大于等于0的整数且应小于等于该类资源的最大需求值)：");
                try {
                    int requestNum = Integer.parseInt(input.nextLine().trim());
                    while (requestNum < 0 || requestNum > processRes.get(j).getMaxResProcess().get(a)) {
                        System.out.println("输入不合法，请重新输入进程" + processRes.get(j).getName() + "对" + (char) b + "类资源的资源申请值(大于等于0的整数且应小于等于该类资源的最大需求值)：");
                        requestNum = Integer.parseInt(input.nextLine().trim());
                    }
                    int allocation = processRes.get(j).getMaxResProcess().get(a) - requestNum;
                    request.add(requestNum);
                    allocated.add(allocation);
                    a++;
                    b++;
                } catch (Exception e) {
                    System.out.println("输入不合法，请重新输入");
                }
            }
            b = 65;
            boolean continueCycle = true;
            int add = 0;
            for (int f = 0; f < resNum; f++) {
                add += request.get(f);
            }
            if (add == 0) {
                System.out.println("对各类资源的申请值不能均为0,提出资源分配请求失败");
                continueCycle = false;
            }
            if (continueCycle) {
                processRes.get(j).setRequestResProcess(request);
                processRes.get(j).setAllocatedResProcess(allocated);
                if (processRes.size() - 1 > j) {
                    boolean v = true;
                    while (processRes.get(j + 1).getRequestResProcess().size() != 0 && v) {
                        j++;
                        if (processRes.size() - 1 == j) {
                            v = false;
                            j--;
                        }
                    }
                }
                if (processRes.size() - 1 == j + 1 && processRes.getLast().getRequestResProcess().size() != 0) {
                    j++;
                }
                j++;
            }
        }
        return true;
    }

    public boolean algorithm() {
        int i = 0;
        int j = 0;
        int q = 0;
        int w = processRes.size();
        LinkedList<Process> temporaryProcessRes = new LinkedList<Process>();
        temporaryProcessRes = (LinkedList<Process>) processRes.clone();
        LinkedList<Integer> temporaryCPURes = new LinkedList<Integer>();
        temporaryCPURes = (LinkedList<Integer>) cpuRes.clone();
        while (i < w) {
            while (j < processRes.size()) {
                int k = 0;
                while (k < resNum) {
                    if (processRes.get(j).getRequestResProcess().get(k) > cpuRes.get(k)) {
                        q = -1;
                    }
                    k++;
                }
                if (q == 0) {
                    for (int p = 0; p < resNum; p++) {
                        cpuRes.set(p, processRes.get(j).getRequestResProcess().get(p) + cpuRes.get(p));
                    }
                    security.offer(processRes.get(j));
                    processRes.remove(j);
                }
                j++;
                q = 0;
            }
            i++;
            if (processRes.size() == 0) {
                i = w;
            } else {
                j = 0;
            }
        }
        if (security.size() == w) {
            processRes.clear();
            processRes = (LinkedList<Process>) security.clone();
            return true;
        } else {
            System.out.println("安全序列不存在");
            processRes = temporaryProcessRes;
            cpuRes = temporaryCPURes;
            security.clear();
            return false;
        }
    }

    public int getIndexByName(String name) {
        int a = -1;
        for (int h = 0; h < processRes.size(); h++) {
            if (processRes.get(h).getName().equals(name)) {
                a = h;
            }
        }
        return a;
    }

    public boolean confirmChange() {
        if (processRes.size() == 0) {
            System.out.println("当前不存在进程，无法修改进程信息");
            return false;
        }
        Scanner input = new Scanner(System.in);
        if (security.size() > 0) {
            System.out.println("已存在安全序列，修改进程信息安全序列会被删除，确定要创建吗？（输入y以继续，随意输入以取消）");
            String inputString = input.nextLine();
            if (inputString.equals("y")) {
                security.clear();
                if (processRes.size() == 1) {
                    change(0);
                    return true;
                } else {
                    System.out.println("请输入想要修改的进程名：");
                    int f = getIndexByName(input.nextLine());
                    if (f > -1) {
                        change(f);
                        return true;
                    } else {
                        System.out.println("输入的进程名不存在，无法修改进程信息");
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            if (processRes.size() == 1) {
                change(0);
                return true;
            }
            System.out.println("请输入想要修改的进程名：");
            int y = getIndexByName(input.nextLine());
            if (y > -1) {
                change(y);
                return true;
            } else {
                System.out.println("输入的进程名不存在，无法修改进程信息");
                return false;
            }
        }
    }

    public boolean change(int index) {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入想要修改的信息类别（修改名称输入name，修改最大资源需求值输入max，修改资源申请值输入request，随意输入以取消）");
        String changeInfo = input.nextLine();
        if (changeInfo.equals("name")) {
            System.out.println("请输入新名称：");
            String newName = input.nextLine();
            while (newName.length() == 0) {
                System.out.println("进程名不能为空，请重新输入：");
                newName = input.nextLine();
            }
            if (haveSameName(newName)) {
                processRes.get(index).setName(newName);
                return true;
            } else {
                System.out.println("该进程名已存在，修改失败");
                return false;
            }
        }
        if (changeInfo.equals("max")) {
            System.out.println("请输入新的最大资源需求值：");
            LinkedList<Integer> max = new LinkedList<Integer>();
            int a = 0;
            int b = 65;
            while (a < resNum) {
                System.out.println("请输入新的对" + (char) b + "类资源的最大需求值(大于等于0的整数)：");
                try {
                    int maxNum = Integer.parseInt(input.nextLine().trim());
                    while (maxNum < 0) {
                        System.out.println("输入不合法，请重新输入新的对" + (char) b + "类资源的最大需求值(大于等于0的整数)：");
                        maxNum = Integer.parseInt(input.nextLine().trim());
                    }
                    max.add(maxNum);
                    a++;
                    b++;
                } catch (Exception e) {
                    System.out.println("输入不合法，请重新输入");
                }
            }
            b = 65;
            processRes.get(index).setMaxResProcess(max);
            return true;
        }
        if (changeInfo.equals("request")) {
            int a = 0;
            int b = 65;
            LinkedList<Integer> request = new LinkedList<Integer>();
            LinkedList<Integer> allocated = new LinkedList<Integer>();
            while (a < resNum) {
                System.out.println("请输入新的对" + (char) b + "类资源的资源申请值(大于等于0的整数且应小于等于该类资源的最大需求值)：");
                try {
                    int requestNum = Integer.parseInt(input.nextLine().trim());
                    while (requestNum < 0 || requestNum > processRes.get(index).getMaxResProcess().get(a)) {
                        System.out.println("输入不合法，请重新输入新的对" + (char) b + "类资源的资源申请值(大于等于0的整数且应小于等于该类资源的最大需求值)：");
                        requestNum = Integer.parseInt(input.nextLine().trim());
                    }
                    int allocation = processRes.get(index).getMaxResProcess().get(a) - requestNum;
                    request.add(requestNum);
                    allocated.add(allocation);
                    a++;
                    b++;
                } catch (Exception e) {
                    System.out.println("输入不合法，请重新输入");
                }
            }
            b = 65;
            int add = 0;
            for (int f = 0; f < resNum; f++) {
                add += request.get(f);
            }
            if (add == 0) {
                System.out.println("对各类资源的申请值不能均为0,修改失败");
                return false;
            }
            processRes.get(index).setRequestResProcess(request);
            processRes.get(index).setAllocatedResProcess(allocated);
            return true;
        }
        return false;
    }

    public boolean destroy() {
        Scanner input = new Scanner(System.in);
        if (processRes.size() == 0) {
            System.out.println("当前不存在进程，无法撤销进程");
            return false;
        }
        if (security.size() != 0) {
            System.out.println("已存在安全序列，修改进程信息安全序列会被删除，确定要创建吗？（输入y以继续，随意输入以取消）");
            String inputString = input.nextLine();
            if (inputString.equals("y")) {
                security.clear();
                if (processRes.size() == 1) {
                    processRes.remove(0);
                    return true;
                } else {
                    System.out.println("请输入想要撤销的进程名：");
                    int f = getIndexByName(input.nextLine());
                    if (f > -1) {
                        processRes.remove(f);
                        return true;
                    } else {
                        System.out.println("输入的进程名不存在，无法撤销进程");
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (processRes.size() == 1) {
            processRes.remove(0);
            return true;
        } else {
            System.out.println("请输入想要撤销的进程名：");
            int f = getIndexByName(input.nextLine());
            if (f > -1) {
                processRes.remove(f);
                return true;
            } else {
                System.out.println("输入的进程名不存在，无法撤销进程");
                return false;
            }
        }
    }

    public void start() {
        //初始化CPU各类资源可用值（获取资源的类别数目 + 获取各类资源的具体可用值）
        System.out.println("请输入资源类别的个数（大于0且小于等于5的整数）：");
        Scanner scan = new Scanner(System.in);
        int a = 0;
        while (a == 0) {
            try {
                resNum = Integer.parseInt(scan.nextLine().trim());
                while (resNum < 0 || resNum > 5) {
                    System.out.println("输入不合法，请重新输入资源类别的个数（大于0且小于等于5的整数）：");
                    resNum = Integer.parseInt(scan.nextLine().trim());//trim()方法用于去除输入值两端的空格
                }
                a = 1;
            } catch (Exception e) {
                System.out.println("输入不合法，请重新输入资源类别的个数（大于0且小于等于5的整数）：");
            }
        }
        LinkedList<Integer> cpuResNums = new LinkedList<Integer>();
        int b = 65;
        while (a < resNum + 1) {
            System.out.println("请输入" + (char) b + "类资源在CPU中的可用值(大于等于0的整数)：");
            try {
                int cpuResNum = Integer.parseInt(scan.nextLine().trim());
                while (cpuResNum < 0) {
                    System.out.println("输入不合法，请重新输入" + (char) b + "类资源在CPU中的可用值(大于等于0的整数)：");
                    cpuResNum = Integer.parseInt(scan.nextLine().trim());
                }
                cpuResNums.add(cpuResNum);
                cpuRes = cpuResNums;
                a++;
                b++;
            } catch (Exception e) {
                System.out.println("输入不合法，请重新输入");
            }
        }
        System.out.println("CPU各类资源可用值初始化成功！");
    }

    public LinkedList<Integer> getFirstCPURes() {
        LinkedList<Integer> firstCPURes = new LinkedList<Integer>();
        for (int q = 0; q < cpuRes.size(); q++) {
            int e = 0;
            e = cpuRes.get(q);
            firstCPURes.add(e);
        }
        return firstCPURes;
    }


    public static void main(String args[]) {
        System.out.println("银行家算法模拟程序");
        BankerAlgorithm bankerAlgorithm = new BankerAlgorithm();
        bankerAlgorithm.start();
        LinkedList<Integer> firstCPURes = new LinkedList<Integer>();
        firstCPURes = bankerAlgorithm.getFirstCPURes();
        Scanner scan = new Scanner(System.in);
        int select = 0;
        while (true) {
            System.out.println("20001:创建进程\t\t\t20002:提出资源分配请求以计算安全序列");
            System.out.println("20003:修改进程信息\t\t20004:撤销进程");
            System.out.println("20005:初始化\t\t\t\t20000:退出");
            System.out.println("请输入数字以实现相应的功能：");
            int c = 0;
            while (c == 0) {
                try {
                    select = Integer.parseInt(scan.nextLine().trim());
                    while (select < 20000 || select > 20005) {
                        System.out.println("输入不合法，请重新输入数字以实现相应的功能：");
                        select = Integer.parseInt(scan.nextLine().trim());
                    }
                    c = 1;
                } catch (Exception e) {
                    System.out.println("输入不合法，请重新输入数字以实现相应的功能：");
                }
            }
            LinkedList<Integer> temporaryCPURes = new LinkedList<Integer>();
            temporaryCPURes = (LinkedList<Integer>) bankerAlgorithm.getCpuRes().clone();
            switch (select) {
                case 20001:
                    if (bankerAlgorithm.confirmCreate()) {
                        bankerAlgorithm.create();
                    }
                    break;
                case 20002:
                    if (bankerAlgorithm.getProcessRes().size() == 0) {
                        System.out.println("尚未创建进程，无法提出资源分配请求");
                    } else if (bankerAlgorithm.getSecurity().size() != 0) {
                        System.out.println("当前安全序列已存在，无法提出资源分配请求");
                    } else {
                        if (bankerAlgorithm.request()) {
                            bankerAlgorithm.algorithm();
                        }
                    }
                    break;
                case 20003:
                    bankerAlgorithm.confirmChange();
                    break;
                case 20004:
                    if (bankerAlgorithm.destroy()) {
                        System.out.println("撤销进程成功");
                    }
                    break;
                case 20005:
                    System.out.println("确定进行初始化吗？这将会删除所有信息（输入y以继续，随意输入以取消）");
                    if (scan.nextLine().equals("y")) {
                        bankerAlgorithm.getCpuRes().clear();
                        bankerAlgorithm.setResNum(0);
                        bankerAlgorithm.getProcessRes().clear();
                        bankerAlgorithm.getSecurity().clear();
                        bankerAlgorithm.getFirstCPURes().clear();
                        bankerAlgorithm.getFinalCPURes().clear();
                        bankerAlgorithm.start();
                    }
                    break;
                case 20000:
                    System.exit(0);//正常退出程序
            }
            System.out.println("****************************************************************************************************************");
            System.out.println("\t资源情况\t\t\t最大资源需求值\t\t\t已分配资源值\t\t\t申请资源值\t\t\t可用资源值（CPU）");
            System.out.println("进程\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + firstCPURes);
            if (bankerAlgorithm.getSecurity().size() == 0) {
                for (int i = 0; i < bankerAlgorithm.getProcessRes().size(); i++) {
                    System.out.println(bankerAlgorithm.getProcessRes().get(i).getName() + "\t\t\t\t" + bankerAlgorithm.getProcessRes().get(i).getMaxResProcess() + "\t\t\t\t" + bankerAlgorithm.getProcessRes().get(i).getAllocatedResProcess() + "\t\t\t\t" + bankerAlgorithm.getProcessRes().get(i).getRequestResProcess());
                }
            } else {
                for (int m = 0; m < bankerAlgorithm.getSecurity().size(); m++) {
                    LinkedList<Integer> fc = new LinkedList<Integer>();
                    for (int n = 0; n < bankerAlgorithm.getResNum(); n++) {
                        fc.add(temporaryCPURes.get(n) + bankerAlgorithm.getSecurity().get(m).getMaxResProcess().get(n));
                        temporaryCPURes.set(n, fc.get(n));
                    }
                    bankerAlgorithm.getFinalCPURes().add(fc);
                }
                for (int i = 0; i < bankerAlgorithm.getSecurity().size(); i++) {
                    System.out.println(bankerAlgorithm.getSecurity().get(i).getName() + "\t\t\t\t" + bankerAlgorithm.getSecurity().get(i).getMaxResProcess() + "\t\t\t\t" + bankerAlgorithm.getSecurity().get(i).getAllocatedResProcess() + "\t\t\t\t" + bankerAlgorithm.getSecurity().get(i).getRequestResProcess() + "\t\t\t\t" + bankerAlgorithm.getFinalCPURes().get(i));
                }
                System.out.print("安全序列存在，为：");
                for (int o = 0; o < bankerAlgorithm.getSecurity().size(); o++) {
                    System.out.print("\t" + bankerAlgorithm.getSecurity().get(o).getName() + "\t");
                }
                System.out.println();
            }
            System.out.println("****************************************************************************************************************");
        }
    }
}
