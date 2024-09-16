package com.app.operation;

import com.app.ModeRepository;
import com.app.dto.ModelDTO;
import com.app.entity.FileModel;
import com.app.entity.Mode;
import com.app.service.FileStorageService;
import com.app.service.impl.LocalFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    private static final String HOME_PAGE = "home";
    private static final String LIST_PAGE = "list";

    @Autowired
//    @Qualifier("localFileStorage")
    @Qualifier("remoteFileStorage")
    private FileStorageService fileStorageService;

    @Autowired
    private ModeRepository modeRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loadHomePage(Model model) {
        log.info("Entering loadHomePage() method");

        ModelDTO modelDTO = new ModelDTO();

        model.addAttribute("model", modelDTO);
        model.addAttribute("pageTitle", "Home");
        log.info("Exiting loadHomePage() method");
        return HOME_PAGE;
    }

    @PostMapping("/")
    public String uploadMultipartFile(@ModelAttribute("model") final ModelDTO modelDTO, @RequestParam("files") MultipartFile[] files, Model model) {

        if(files == null || files.length == 0 || files[0].isEmpty()){
            model.addAttribute("errMsg", "Please attach at least one file !");
            return HOME_PAGE;
        }

        if(modeRepository.findFirstByField1AndField2AndField3(modelDTO.getField1().trim(),
                modelDTO.getField2().trim(), modelDTO.getField3().trim()) != null) {
            model.addAttribute("errMsg", "Value already exists !");
            return HOME_PAGE;
        }

        try {
            Mode  m = new Mode();
            m.setField1(modelDTO.getField1());
            m.setField2(modelDTO.getField2());
            m.setField3(modelDTO.getField3());

            List<FileModel> fileList = new ArrayList<>();
            for (MultipartFile file : files) {
                System.out.println(file);
                String fileName = fileStorageService.storeFile(file);
                fileList.add(new FileModel(fileName));
            }

            m.setFileList(fileList);

            modeRepository.save(m);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return HOME_PAGE;

    }

    @GetMapping("/list")
    public String list( Model model) {

        List<Mode> modeList = modeRepository.findAll();
        model.addAttribute("list", modeList);

        return LIST_PAGE;

    }
}
