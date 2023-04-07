package com.example.student.user.data;

//@Component
//public class UserLoader implements CommandLineRunner {
//    private static final String CSV_FILE_PATH = "C:\\Users\\GeoComputers\\Downloads\\user_data.csv";
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<TribeUser> users = readUsersFromCsvFile(CSV_FILE_PATH);
//        userRepository.saveAll(users);
//    }
//
//    private List<TribeUser> readUsersFromCsvFile(String csvFilePath) throws IOException {
//        List<TribeUser> users = new ArrayList<>();
//
//        File file = ResourceUtils.getFile(csvFilePath);
//        FileInputStream inputStream = new FileInputStream(file);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            String[] data = line.split(",");
//
//            System.out.println(data[1]);
//            TribeUser user = new TribeUser();
//            user.setName(data[0]);
//            user.setEmail(data[1]);
//            user.setFirebaseId(data[2]);
//            user.setTimezone(data[4]);
//
//            users.add(user);
//        }
//        bufferedReader.close();
//
//        return users;
//    }
//}
