[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Bnm1Ovni)
## CSC 435 Extra Credit Assignment (Winter 2025)
**Jarvis College of Computing and Digital Media - DePaul University**

**Student**: Mohammed Musaddiq Vavartar (mvavarta@depaul.edu)  
**Solution programming language**: Java

### Requirements

If you are implementing your solution in C++ you will need to have GCC 14.x and CMake 3.28.x installed on your system. You will also need to install the ZeroMQ libraries and development files. On Ubuntu 24.04 LTS you can install GCC and set it as default compiler using the following commands:
```
sudo apt install build-essential cmake g++-14 gcc-14 cmake
sudo update-alternatives --remove-all gcc
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-13 130
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-14 140
sudo update-alternatives --remove-all g++
sudo update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-13 130
sudo update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-14 140
sudo apt install pkg-config libzmq3-dev libzmq5 libczmq-dev libczmq4 cppzmq-dev
```

You will also need to install the JeroMQ (Java ZeroMQ) library and development jar. On Ubuntu 24.04 LTS you can install Java, Maven and JeroMQ using the following commands:

```
sudo apt install openjdk-21-jdk maven libjeromq-java
```

### Setup

There are 3 datasets (dataset1_client_server, dataset2_client_server, dataset3_client_server) that you need to use to evaluate the indexing performance of your solution.
Before you can evaluate your solution you need to download the datasets. You can download the datasets from the following link:

https://depauledu-my.sharepoint.com/:f:/g/personal/aorhean_depaul_edu/Ej4obLnAKMdFh1Hidzd1t1oBHY7IvgqXoLdKRg-buoiisw?e=SWLALa

After you finished downloading the datasets copy them to the dataset directory (create the directory if it does not exist).
Here is an example on how you can copy Dataset1 to the remote machine and how to unzip the dataset:

```
remote-computer$ mkdir datasets
local-computer$ scp dataset1_client_server.zip cc@<remote-ip>:<path-to-repo>/datasets/.
remote-computer$ cd <path-to-repo>/datasets
remote-computer$ unzip dataset1_client_server.zip
```

### C++ solution
#### How to build/compile

To build the C++ solution use the following commands:
```
cd app-cpp
mkdir build
cmake -S . -B build
cmake --build build --config Release
```

#### How to run application

To run the C++ server (after you build the project) use the following command:
```
./build/file-retrieval-server <port> <number of worker threads>
> <quit>
```

To run the C++ client (after you build the project) use the following command:
```
./build/file-retrieval-client
> <connect | get_info | index | search | quit>
```

#### Example (2 clients and 1 server)

**Step 1:** start the server with 2 worker threads:

Server
```
./build/file-retrieval-server 12345 2
>
```

**Step 2:** start the clients and connect them to the server:

Client 1
```
./build/file-retrieval-client
> connect 127.0.0.1 12345
Connection successful!
> get_info
Client ID: 1
```

Client 2
```
./build/file-retrieval-client
> connect 127.0.0.1 12345
Connection successful!
> get_info
Client ID: 2
```

**Step 3:** index files from the clients:

Client 1
```
> index ../datasets/dataset1_client_server/2_clients/client_1
Completed indexing 68383239 bytes of data
Completed indexing in 2.974 seconds
```

Client 2
```
> index ../datasets/dataset1_client_server/2_clients/client_2
Completed indexing 65864138 bytes of data
Completed indexing in 2.386 seconds
```

**Step 4:** search files from the clients:

Client 1
```
> search the
Search completed in 0.4 seconds
Search results (top 10 out of 0):
> search child-like
Search completed in 2.8 seconds
Search results (top 10 out of 15):
* Client 2:folder7/Document10926.txt:4
* Client 1:folder3/Document10379.txt:3
* Client 2:folder6/Document10866.txt:2
* Client 2:folder8/Document1108.txt:1
* Client 2:folder7/folderD/Document11050.txt:1
* Client 2:folder6/Document10848.txt:1
* Client 2:folder6/Document1082.txt:1
* Client 1:folder4/Document10681.txt:1
* Client 1:folder4/Document10669.txt:1
* Client 1:folder3/Document10387.txt:1
```

Client 2
```
> search distortion AND adaptation
Search completed in 3.27 seconds
Search results (top 10 out of 4):
* Client 2:folder7/folderC/Document10998.txt:6
* Client 1:folder4/Document10516.txt:3
* Client 2:folder8/Document11159.txt:2
* Client 2:folder8/Document11157.txt:2
```

**Step 5:** close and disconnect the clients:

Client 1
```
> quit
```

Client 2
```
> quit
```

**Step 6:** close the server:

Server
```
> quit
```

#### Example (benchmark with 2 clients and 1 server)

**Step 1:** start the server with 2 worker threads:

Server
```
./build/file-retrieval-server 12345 2
>
```

**Step 2:** start the benchmark:

Benchmark
```
./build/file-retrieval-benchmark 127.0.0.1 12345 2 ../datasets/dataset1_client_server/2_clients/client_1 ../datasets/dataset1_client_server/2_clients/client_2
Completed indexing 134247377 bytes of data
Completed indexing in 6.015 seconds
Searching the
Search completed in 0.4 seconds
Search results (top 10 out of 0):
Searching child-like
Search completed in 2.8 seconds
Search results (top 10 out of 15):
* Client 2:folder7/Document10926.txt:4
* Client 1:folder3/Document10379.txt:3
* Client 2:folder6/Document10866.txt:2
* Client 2:folder8/Document1108.txt:1
* Client 2:folder7/folderD/Document11050.txt:1
* Client 2:folder6/Document10848.txt:1
* Client 2:folder6/Document1082.txt:1
* Client 1:folder4/Document10681.txt:1
* Client 1:folder4/Document10669.txt:1
* Client 1:folder3/Document10387.txt:1
Searching distortion AND adaptation
Search completed in 3.27 seconds
Search results (top 10 out of 4):
* Client 2:folder7/folderC/Document10998.txt:6
* Client 1:folder4/Document10516.txt:3
* Client 2:folder8/Document11159.txt:2
* Client 2:folder8/Document11157.txt:2
```

**Step 3:** close the server:

Server
```
> quit
```

### Java solution
#### How to build/compile

To build the Java solution use the following commands:
```
cd app-java
mvn compile
mvn package
```

#### How to run application

To run the Java server (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalServer <port> <number of worker threads>
> <list | quit>
```

To run the Java client (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalClient
> <connect | get_info | index | search | quit>
```

To run the Java benchmark (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalBenchmark <server IP> <server port> <number of clients> [<dataset path>]
```

#### Example (2 clients and 1 server)

**Step 1:** start the server with 2 worker threads:

Server
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalServer 12345 2
>
```

**Step 2:** start the clients and connect them to the server:

Client 1
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
Connection successful!
> get_info
Client ID: 1
```

Client 2
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
Connection successful!
> get_info
Client ID: 2
```

**Step 3:** index files from the clients:

Client 1
```
> index ../datasets/dataset1_client_server/2_clients/client_1
Completed indexing 68383239 bytes of data
Completed indexing in 2.974 seconds
```

Client 2
```
> index ../datasets/dataset1_client_server/2_clients/client_2
Completed indexing 65864138 bytes of data
Completed indexing in 2.386 seconds
```

**Step 4:** search files from the clients:

Client 1
```
> search the
Search completed in 0.4 seconds
Search results (top 10 out of 0):
> search child-like
Search completed in 2.8 seconds
Search results (top 10 out of 15):
* Client 2:folder7/Document10926.txt:4
* Client 1:folder3/Document10379.txt:3
* Client 2:folder6/Document10866.txt:2
* Client 2:folder8/Document1108.txt:1
* Client 2:folder7/folderD/Document11050.txt:1
* Client 2:folder6/Document10848.txt:1
* Client 2:folder6/Document1082.txt:1
* Client 1:folder4/Document10681.txt:1
* Client 1:folder4/Document10669.txt:1
* Client 1:folder3/Document10387.txt:1
```

Client 2
```
> search distortion AND adaptation
Search completed in 3.27 seconds
Search results (top 10 out of 4):
* Client 2:folder7/folderC/Document10998.txt:6
* Client 1:folder4/Document10516.txt:3
* Client 2:folder8/Document11159.txt:2
* Client 2:folder8/Document11157.txt:2
```

**Step 5:** close and disconnect the clients:

Client 1
```
> quit
```

Client 2
```
> quit
```

**Step 6:** close the server:

Server
```
> quit
```

#### Example (benchmark with 2 clients and 1 server)

**Step 1:** start the server with 2 worker threads:

Server
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalServer 12345 2
>
```

**Step 2:** start the benchmark:

Benchmark
```
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalBenchmark 127.0.0.1 12345 2 ../datasets/dataset1_client_server/2_clients/client_1 ../datasets/dataset1_client_server/2_clients/client_2
Completed indexing 134247377 bytes of data
Completed indexing in 6.015 seconds
Searching the
Search completed in 0.4 seconds
Search results (top 10 out of 0):
Searching child-like
Search completed in 2.8 seconds
Search results (top 10 out of 15):
* Client 2:folder7/Document10926.txt:4
* Client 1:folder3/Document10379.txt:3
* Client 2:folder6/Document10866.txt:2
* Client 2:folder8/Document1108.txt:1
* Client 2:folder7/folderD/Document11050.txt:1
* Client 2:folder6/Document10848.txt:1
* Client 2:folder6/Document1082.txt:1
* Client 1:folder4/Document10681.txt:1
* Client 1:folder4/Document10669.txt:1
* Client 1:folder3/Document10387.txt:1
Searching distortion AND adaptation
Search completed in 3.27 seconds
Search results (top 10 out of 4):
* Client 2:folder7/folderC/Document10998.txt:6
* Client 1:folder4/Document10516.txt:3
* Client 2:folder8/Document11159.txt:2
* Client 2:folder8/Document11157.txt:2
```

**Step 3:** close the server:

Server
```
> quit
```
