from operator import add, gt, sub

def safe(allocation, max, available):
    work = available
    finished = [False for process in allocation]
    max_available = list(map(add, available, [sum(i) for i in zip(*allocation)]))
    needs = [list(map(sub, m, a)) for m, a in zip(max,allocation)]
    done = False
    while not done:
        done = True
        for pid in [i for i, x in enumerate(finished) if not x]:
            if any(map(gt, needs[pid], work)): continue # process needs too much, try next process
            else: # process needs can be satisfied, update resources available
                work = list(map(add, work, allocation[pid]))
                #print str(pid) + " is done" # safe sequence
                finished[pid] = True
                done = False
    return True if all(finished) else False

allocation1 = [
    [0,1,0],
    [2,0,0],
    [3,0,2],
    [2,1,1],
    [0,0,2],
]
max_claim1 = [
    [7,5,3],
    [3,2,2],
    [9,0,2],
    [2,2,2],
    [4,3,3],
]
available1 = [3,3,2]
ex1 = [allocation1,max_claim1,available1]

allocation2 = [
    [0,0,1,2],
    [1,0,0,0],
    [1,3,5,4],
    [0,6,3,2],
    [0,0,1,4],
]
max_claim2 = [
    [0,0,1,2],
    [1,7,5,0],
    [2,3,5,6],
    [0,6,5,2],
    [0,6,5,6],
]
available2 = [1,5,2,0]
ex2 = [allocation2,max_claim2,available2]

print "Is a safe state." if safe(*ex2) else "Not a safe state."