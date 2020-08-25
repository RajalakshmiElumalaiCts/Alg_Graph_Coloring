package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileProcessor {
	
	public List<Integer> readInput(String filePath) {
		List<Integer> integerFileContent = null;
		
		Path path = Paths.get(filePath);
		
		if(path.isAbsolute()){
			try {
				List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
				//O(n)
				integerFileContent = allLines.stream()
						.map(val -> val.trim())
						.map(val -> Integer.parseInt(val))
						.collect(Collectors.toList());
			} catch (IOException e) {
				System.out.println(" Unable to read input file"+ e.getMessage());
				e.printStackTrace();
			}
			
		}
		return integerFileContent;		
	}

}
