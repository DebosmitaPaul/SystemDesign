import java.util.HashMap;
import java.util.Map;

public class LFU {
    public static void main(String[] args) {
        LFU cache = new LFU(2);
        cache.put(1,10);
        cache.put(2,20);
        System.out.println(cache.get(1));
        cache.put(3,30);
        System.out.println(cache.get(2));
        System.out.println(cache.get(3));
        cache.put(4,40);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
    final int capacity;
    int minfreq;
    Map<Integer, DLNode> cacheMap;
    Map<Integer, DLLinkedList> frequencyMap;
    LFU(int capacity){
        this.capacity= capacity;
        cacheMap = new HashMap<>();
        frequencyMap = new HashMap<>();
        minfreq =1;
    }
    void put(int key, int value) {
        if(cacheMap.containsKey(key)){
            DLNode node = cacheMap.get(key);
            int freq = node.freq;

            DLLinkedList oldListNode = frequencyMap.get(freq);
            oldListNode.removeInBetween(node);

            if(freq == minfreq && oldListNode.isEmpty()){
                minfreq++;
            }

            node.freq++;
            node.val = value;
//            if(frequencyMap.get(node.freq) == null){
//                frequencyMap.put(key, new DLLinkedList());
//            }
//            frequencyMap.putIfAbsent(key, new DLLinkedList());

            DLLinkedList newListNode = frequencyMap.computeIfAbsent(node.freq, k -> new DLLinkedList());
            newListNode.addFront(node);
        } else {
            if(cacheMap.size() >= this.capacity){
               DLLinkedList associatedNode  = frequencyMap.get(minfreq);
               DLNode removeNode = associatedNode.removeFromLast();
               cacheMap.remove(removeNode.key);
            }
//            if(frequencyMap.get(1) == null){
//                frequencyMap.put(1, new DLLinkedList());
//            }
            DLLinkedList alllistnodes = frequencyMap.computeIfAbsent(1,k -> new DLLinkedList()); //.get(1);
            DLNode newNode = new DLNode(key,value);
            newNode.freq = 1;
            alllistnodes.addFront(newNode);
            cacheMap.put(key,newNode);
            minfreq =1;
        }
    }
    int get(int key){
        if(cacheMap.containsKey(key)){
            DLNode node = cacheMap.get(key);
            int freq = node.freq;

            DLLinkedList oldListNode = frequencyMap.get(freq);
            oldListNode.removeInBetween(node);
            node.freq++;

            //min freq update
            if(freq == minfreq && oldListNode.isEmpty()){
                minfreq++;
            }

            if(frequencyMap.get(node.freq) == null){
                frequencyMap.put(node.freq, new DLLinkedList());
            }
            DLLinkedList newListNode = frequencyMap.get(node.freq);
            newListNode.addFront(node);
            return node.val;
        }
        return -1;
    }
}

class DLNode{
    int key;
    int val;
    int freq;
    DLNode prev;
    DLNode next;
    DLNode(int key, int val){
        this.key = key;
        this.val = val;
        this.prev = null;
        this.next = null;
    }
}
class DLLinkedList{
    DLNode head;
    DLNode tail;
    DLLinkedList(){
        this.head = new DLNode(-1,-1);
        this.tail = new DLNode(-1,-1);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    void addFront(DLNode node){
        DLNode nextN = head.next;
        node.next = nextN;
        nextN.prev = node;
        head.next = node;
        node.prev = head;
    }
    DLNode removeFromLast(){
        DLNode node = tail.prev;
        tail.prev = node.prev;
        node.prev.next = tail;
        return node;
    }
    void removeInBetween(DLNode node){
        DLNode prevNode = node.prev;
        DLNode nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
    boolean isEmpty(){
        if(head.next == tail && tail.prev == head){
            return true;
        }
        return false;
    }
}


/*
10
-1
30
-1
30
40

*/
