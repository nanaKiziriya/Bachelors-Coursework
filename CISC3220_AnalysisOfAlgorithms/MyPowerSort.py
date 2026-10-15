###############################################
# FIND THIS CODE AND RUN IT EASILY AT
# https://colab.research.google.com/drive/1ZvgmY4cQmAnkF71xEu8f0UMT25WAUtnU?usp=sharing
###############################################

# Assignment:

#     Accept an unsorted array of integers.
#     Sort the array using your own implementation of the algorithm.
#     Print the array before and after sorting.
#     Test your implementation on several different arrays, including:

#         An already sorted array
#         A reverse-sorted array
#         An array containing duplicate values

#     [DONE] Include a short explanation of why your algorithm has O(n log n) runtime.


#@markdown Imports

import random
import time
import math
import matplotlib.pyplot as plt
from ctypes import c_int
from collections import deque


#@markdown run (obj class)

class run :
  def __init__(self,
               start,
               end,
               dir, # 1 or -1
               power = 0, # power in relation to NEXT run
               ):
    self.start = start
    self.end = end
    self.size = end-start
    self.dir = dir
    self.power = power

  def __str__(self) :
    return f'({self.start},{self.end},{self.size},{self.dir},{self.power})'


#@markdown power() standard version

def power_ver_standard(arr,r1,r2) :

  if len(arr)==0 :
    print('error')
    return -1

  if not (r1 and r2):
    return 0 # zero merge precendence if no merge possible

  # each run is characterized by its centerpoint (r.start+r.size/2)
  # starts out scaled down to [0,1] (/len(arr))
  node1 = ((r1.start+r1.end)/2)/len(arr)
  node2 = ((r2.start+r2.end)/2)/len(arr)

  # Concept: find how deep the shared branch of the these nodes are in a binary tree, i.e. where these nodes finally split off from each other
  # Visualization: Keep cutting the array in half until you finally separate the 2 nodes
  # Utility: merge priority goes to pairs of 'closer' runs

  level = 1
  # level corresponds to # times array is halved before nodes are separated from each other

  while(int(node1)==int(node2)):
    node1*=2
    node2*=2
    level+=1

  return level


#@markdown power() experimental version

def power_ver_experimental_1(arr,r1,r2) :

  if len(arr)==0 :
    print('error')
    return -1

  if not (r1 and r2):
    return len(arr)-0 # zero merge precendence if no merge possible

  return len(arr) - abs(r1.size - r2.size)


#@markdown power() experimental version

def power_ver_experimental_2(arr,r1,r2) :

  if len(arr)==0 :
    print('error')
    return -1

  if not (r1 and r2):
    return len(arr)-0 # zero merge precendence if no merge possible

  # return len(arr) - abs(r1.size - r2.size) # this was already an improvement on the standard power function
  return len(arr) - r1.size - r2.size # priority to smaller combined length


#@markdown VERSIONS OF MERGE CRITERIA (power function)

# nanasort versions
power = (power_ver_standard, power_ver_experimental_1, power_ver_experimental_2)
ver_names = ['Standard Power', 'Experimental 1 (least len diff)', 'Experimental 2 (least merge len)']
ver_colors = ['blue','purple','red']

# for comparing to built-in sort
ver_names += ['Python\'s Built-in Sort']
ver_colors += ['black']


#@markdown next_run()

# local vars: end, dir
# global ref: updates current.power if current
# description: finds next run after current (check empty)
def next_run(ver,arr,current=None) :

  start = 0 if not current else current.end

  if start>=len(arr) :
    return None # empty run

  end = start+1 # end is exclusive

  # skip past duplicate values before determining direction
  while(end<len(arr) and arr[end]==arr[start]) :
    end+=1

  dir = -1 if end<len(arr) and arr[end]<arr[start] else 1

  while(end<len(arr) and arr[end]*dir>=arr[end-1]*dir):
    end+=1 # find end of run

  next_run = run(start,end,dir)

  if current:
    current.power = power[ver](arr,current,next_run)

  return next_run


#@markdown merge()

# local vars: prev, M, p, p_stop, c, c_stop
# global ref: stack[-1], current, next
def merge(ver,arr,stack:deque,current,next) :

  if not stack :
    return

  prev = stack.pop()

  M = [None]*(prev.size+current.size) # empty container for merged run, to be copied back to original array at the end

  # Mark start/stop of each run as determined by its direction
  # vars: p, p_stop, c, c_stop; stops are exclusive
  p = prev.start if prev.dir == 1 else prev.end - 1
  p_stop = prev.end if prev.dir == 1 else prev.start - 1
  c = current.start if current.dir == 1 else current.end - 1
  c_stop = current.end if current.dir == 1 else current.start - 1

  for _ in range(len(M)) :
    if c==c_stop or p!=p_stop and arr[p]<=arr[c] :
      M[_] = arr[p]
      p += prev.dir
    else :
      M[_] = arr[c]
      c += current.dir

  arr[prev.start : current.end] = M

  current.start = prev.start
  current.dir = 1
  current.size = current.end-current.start
  current.power = power[ver](arr,current,next)
  if stack :
    stack[-1].power = power[ver](arr,stack[-1],current)

  return current


#@markdown nanasort()

# global vars: arr, stack, current, next
def nanasort(ver,arr) :

  if ver==-1 : # BUILT-IN SORT (NOT MINE) FOR TESTING/COMPARISON PURPOSES
    arr[:] = sorted(arr[:])
    return

  if len(arr) < 2:
    return

  stack = deque(maxlen = math.ceil(math.log2(len(arr)))) # stack of runs

  current = next_run(ver,arr)
  next = next_run(ver,arr,current)

  while stack or next : # equiv. !(current.start==0&&current.end==len(arr))
    if stack and (not next or stack[-1].power>=current.power) :
      current = merge(ver,arr,stack,current,next) # pops stack, merges into current
      # TODO: merge must also update power of new top of stack
    else :
      stack.append(current)
      current = next
      next = next_run(ver,arr,current) if current else None
    # by this point, current will always exist, stack and next may or may not

  if current.dir==-1 :
    arr[:] = arr[::-1]


#@markdown Demonstrate

def run_demonstrate(vers:list) :

  # Generating arrays to test

  k = 6
  n = 2**k-1
  arr_desc = ('RANDOM','SORTED','REV. SORTED','RANDOM W/ DUPL.','WORST CASE','MAX STACK CASE')
  arrs = (
      [random.randint(0,n*8) for _ in range(n)], # rand, less/no dupl
      list(range(n)), # sorted
      list(range(n,0,-1)), # sorted, rev
      [random.randint(0,int(n//4)) for _ in range(n)], # rand, dupl
      [0,1]*(n//2), # worst case
      [_ for __ in range(1, k + 2) for _ in range(max(1,n//2**__))] # max stacking case
  )


  # Iterating through all test versions

  print('\n\n[ RUNNING ] Demonstration on different types of arrays.')

  if len(vers)==1 :
    print(ver_names[vers[0]],'version --')

  for i in range(len(arrs)) :
    for v,ver in enumerate(vers) :
      arr = (c_int*n)(*arrs[i]) # new copy of array to sort
      if v==0 :
        print('\n    [',arr_desc[i],']')
        print('    Before:',arr[:])
        hm_pre = dict() # pre-check
        for _ in arr :
          if _ in hm_pre : hm_pre[_]+=1
          else : hm_pre[_]=1

      t_init = time.time() # begin stopwatch
      nanasort(ver,arr) # sort
      t_final = time.time() # end stopwatch

      if v==0 :
        print('    After: ',arr[:])
        hm_post = dict() # post-check
        for _ in arr :
          if _ in hm_post : hm_post[_]+=1
          else : hm_post[_]=1
        assert(hm_pre==hm_post)
        assert all(arr[j] <= arr[j + 1] for j in range(len(arr) - 1))


      print(f'    {t_final-t_init:.7E} sec ({ver_names[ver]})' if len(vers)>1 else '')


#@markdown Asymptotic

def run_asymptotic(vers:list) :
  print('\n\n[ RUNNING ] Asymptotic testing of time complexity.')

  min_n = -1 # init to lower bound (exclusive)
  while min_n<0 :
    try:
      min_n = int(input('    Enter int>=0 for minimum array size: '))
    except ValueError:
      print('    Try again! Must be nonnegative int.')

  max_n = min_n
  while max_n<=min_n :
    try :
      max_n = int(input('    Enter maximum array size: '))
    except ValueError:
      print('    Try again! Must be greater int than the minimum.')

  jump = 0
  while jump<1 or jump>= max_n-min_n :
    try:
      jump = int(input('    Enter a valid interval for increasing array size: '))
    except ValueError:
      print('    Try again! Must be positive int.')

  rep = 0
  while rep<1 :
    try:
      rep = int(input('    Number of times to repeat for each size: '))
    except ValueError:
      print('    Try again! Must be positive int.')

  print()
  N = range(min_n,max_n,jump)
  _arrs = list(list(random.randint(0,n) for _ in range(n)) for n in N)

  fig, axes = plt.subplots(
      ncols=1 if len(vers) == 1 else 2,
      squeeze=False,
      figsize=(12 if len(vers)>1 else 6, 5)
      )
  subplot1 = axes[0, 0]
  subplot2 = axes[0, 1] if len(vers)>1 else None

  # plotting each test version; plot1 unscaled, plot2 scaled to max==1
  sp2max = 0
  for _ in range(rep) :
    for ver in vers :
      elapsed = [None]*len(_arrs)
      for i,_arr in enumerate(_arrs) :
        arr = (c_int*len(_arr))(*_arr) # new copy of same array to sort

        t_init = time.time() # begin stopwatch
        nanasort(ver,arr) # sort
        t_final = time.time() # end stopwatch

        elapsed[i] = t_final-t_init

      subplot1.plot(N,elapsed,color=ver_colors[ver],label = ver_names[ver] if _==0 else None)

      if len(vers)>1 :
        elapsed = [_e/elapsed[-1] for _e in elapsed]
        subplot2.plot(N,elapsed,color=ver_colors[ver])
        _max = max(elapsed)
        if _max > sp2max : sp2max = _max

  subplot1.set_title('(Unscaled)')
  subplot1.set_xlabel('Size of Array')
  subplot1.set_ylabel('Seconds')
  subplot1.set_xlim(left=0)
  subplot1.set_ylim(bottom=0)

  handles, labels = subplot1.get_legend_handles_labels()

  fig.legend(
      handles, labels,
      loc='lower center',
      bbox_to_anchor=(0.5, -0.02),
      ncol=len(labels)
  )

  if len(vers)>1 :
    subplot2.set_title('(Scaled)')
    subplot2.set_xlabel('Size of Array')
    subplot2.set_ylabel('Ratio of Time')
    subplot2.set_xlim(left=0)
    subplot2.set_ylim(bottom=0, top=sp2max)

  fig.suptitle('Asymptotic Analysis of Time Complexity')

  fig.subplots_adjust(left=0.02, right=0.98, top=0.85, bottom=0.15, wspace=0.2)
  plt.show()


#@markdown Run Appl.
print('''
[ OVERVIEW ]

    Welcome to NanaSort, an improvement (?) on the standard PowerSort merge condition.

    NanaSort, PowerSort, and TimSort share the same time-space complexity:

       |-------Time Complexity--------|--Space Complexity--|
        Best  Average      Worst              Worst
        Ω(n)  Θ(n log(n))  O(n log(n))        O(n)

    The time complexity for NanaSort is O(n log(n)) because the worst case is an unsorted array with all runs of length 2. Each round of merges doubles the run length. At run length k, There are approximately n/2k merge-pairs and 2k comparison-copy operations done for each. Thus, each round of merging involves a total of n comparison-copy operations. Because run length doubles with each round of merging, at most ceil(log2(n/2)) = ceil(log2(n)-1) = ceil(log2(n))-1 <~ log2(n) ~ log(n) rounds of merging are required. In total, that's at worst n optn/round * log(n) rounds = O(n log(n)) operations.

    TimSort improves the average runtime and best-case time complexity of MergeSort by finding pre-sorted ascending runs within the array instead of heuristically resignating runs to begin at size 1. PowerSort then improves on the merge order, improving average runtime. NanaSort then accomodates both ascending and descending runs, improving the average runtime and turning the worst case into another best case.

[ THEORY ]
    - Adjacent powers can never be the same (else implies non-binary tree)

[ OPTIONS ]

    1 - Demonstrate this sort on different types of arrays.
    2 - Asymptotically test the time complexity on random arrays of increasing size. Cool plot included!
    3 - DO BOTH!!!
''')
valid_usr = ('1','2','3')
usr1=None
while usr1==None :
  usr1 = input('    Option: ')
  if usr1 not in valid_usr :
    usr1 = None
    print('    Try again. Enter the number 1, 2, or 3.')

print(f'''
    0 - Merge Condition 0: {ver_names[0]} (PowerSort binary tree branch)
    1 - Merge Condition 1: {ver_names[1]} (smallest difference in run size)
    2 - Merge Condition 2: {ver_names[2]} (smallest combined run size)
    3 - COMPARE ALL!!! (+ built-in sort)
''')

usr2=None
while usr2==None :
  try :
    usr2 = int(input('    Option: '))
    if usr2>len(power) or usr2<0 :
      usr2 = None
      print('    Try again. Enter the number 1, 2, or 3.')
  except ValueError :
    print('    Try again. Enter the number 1, 2, or 3.')

usr2 = [usr2] if usr2<len(power) else list(range(len(power)))+[-1]

if usr1=='1' or usr1=='3':
  run_demonstrate(usr2)
if usr1=='2' or usr1=='3':
  run_asymptotic(usr2)

print('\nThank you for freeing me from my intellectual prison, Human. Goodbye now.')
