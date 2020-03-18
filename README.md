# BTMChainObserver

Die Fachhochschule Nordwestschweiz (FHNW) möchte den Studierenden und Mitarbeitenden eine kostenneutrale Blockchain anbieten. Diese soll für innovative Projekte, Experimente und Schulungszwecke genutzt werden.

Eine Blockchain schützt sich vor Denial of Service (DoS) Attacken mit Transaktionsgebühren. Dieser Schutzmechanismus ist für eine Lernumgebung nicht praktikabel. Für die Studierenden und Mitarbeitenden sollen keine Kosten anfallen. Das Ziel dieser Arbeit ist, einer definierten Benutzergruppe gratis Transaktionen zu gewähren, ohne dabei die Sicherheit der Blockchain zu komprimittieren.

Der Transaktionsmanager ist als Java-Applikation implementiert worden. Mit der Bibliothek web3j wird der jeweils neuste Block auf Transaktionen untersucht. Gefundene gratis Transaktionen werden analysiert.\newline Der Transaktionsmanager verwaltet die Benutzergruppe. Pro Benutzer wird die Anzahl getätigter gratis Transaktionen und die damit verursachten Rechenkosten der Blockchain überwacht. Die FHNW kann für jeden Benutzer individuelle Limiten definieren. Die Dauer der Suspendierung kann ebenfalls individuell festgelegt werden. Um die Datenpersistenz auch bei einem Stopp der Applikation zu gewährleisten, wird der Zustand regelmässig als Datei gespeichert. Bei einem Neustart wird dieser gelesen und wiederhergestellt.
Für die individuelle Suspendierung wird eine Priority-Queue in Kombination mit einem Command Pattern verwendet. So kann auch eine grosse Anzahl Benutzer, mit hoher Ressourceneffizienz, bearbeitet werden. Für die Entwicklung und Testung ist eine private single Node Blockchain verwendet worden.

# Report und Anleitung 

Der komplette Report ist hier verfügbar: [Bachelorthesis](https://github.com/Geryones/BlockChain_TransactionManager/blob/master/Report/out/Report.pdf)

Eine Installationsanleitung ist ebenfalls im Report enthalten.