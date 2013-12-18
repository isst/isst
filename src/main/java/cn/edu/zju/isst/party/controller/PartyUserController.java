package cn.edu.zju.isst.party.controller;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.User;

@Controller
public class PartyUserController extends BaseController {
    @Autowired
    private UserDao userDao;

    @RequestMapping("/login.html")
    public String login(@ModelAttribute("user") LoggedUser user, Model model,
            @RequestParam(value = "returnUrl", required = false, defaultValue = "") String returnUrl) {
        if (user.getId() > 0) {
            return "redirect:index.html";
        }
        model.addAttribute("title", "登录");
        model.addAttribute("returnUrl", returnUrl);
        return "login.page";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
        return "redirect:login.html";
    }

    @RequestMapping(value = "/nickname.html")
    public String loginPost(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }

        model.addAttribute("title", "修改昵称");
        return "nickname.dialog";
    }

    @RequestMapping("/userForm.html")
    public String form(@RequestParam(value = "userId", required = false, defaultValue = "0") int userId, Model model,
            @ModelAttribute("user") LoggedUser user) {
        if (user.getId() != 1) {
            return "redirect:index.html";
        }

        User teacher = null;
        if (userId == 0) {
            teacher = new User();
            teacher.setType(1);
        } else {
            teacher = userDao.getUserById(userId);
        }

        model.addAttribute("user", teacher);
        model.addAttribute("title", userId == 0 ? "添加用户" : "编辑用户");

        return "user-form.dialog";
    }

    @RequestMapping(value = "/userForm", method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder post(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
            @RequestParam("name") String name, @RequestParam("password") String password,
            @RequestParam("fullname") String fullname, @RequestParam("nickname") String nickname, @ModelAttribute("user") LoggedUser user) {
         if (user.getId() != 1) {
             return new ResultHolder("无权限");
         }

        if (name == null || name.equals("")) {
            return new ResultHolder("用户不能为空");
        } else if (id > 0 && !userDao.checkUserName(name, id)) {
            return new ResultHolder("用户名已存在");
        }

        User teacher = new User();
        teacher.setId(id);
        teacher.setName(name);
        teacher.setFullname(fullname);
        teacher.setNickname(nickname);
        teacher.setPassword(password == null || password.equals("") ? null : userDao.encryptPassword(password));
        teacher.setType(1);

        if (teacher.getId() > 0) {
            userDao.update(teacher);
        } else {
            userDao.create(teacher);
        }

        return new ResultHolder(teacher.getId());
    }

    @RequestMapping("/importUserForm.html")
    public String importUserForm(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() != 1) {
            return "redirect:index.html";
        }

        model.addAttribute("title","导入用户");

        return "import-user.dialog";
    }
    
    @RequestMapping(value = "/importUserForm", method = RequestMethod.POST)
    public String importUser(@RequestParam("xlsFile") MultipartFile file, Model model) throws Exception {
        if (file.getContentType().equals("application/vnd.ms-excel")) {
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            int i = -1, count = 0;
            for (Row row : sheet) {
                i++;
                if (i == 0) {
                    continue;
                }

                String id = getStringCellValue(row.getCell(0));
                String name = getStringCellValue(row.getCell(2));
                String idNo = getStringCellValue(row.getCell(4));
                
                if (idNo == null) {
                    continue;
                }
                
                idNo = idNo.trim();
                if (idNo.length() < 6) {
                    idNo = "123456";
                }
                
                if (userDao.userNameExisting(id)) {
                    continue;
                }
                
                User user = new User();
                user.setName(id);
                user.setFullname(name);
                user.setNickname(name);
                user.setPassword(userDao.encryptPassword(idNo.substring(idNo.length() - 6)));
                user.setType(0);
                
                userDao.create(user);
                
                count++;
            }
            
            model.addAttribute("total", i);
            model.addAttribute("count", count);
        } else {
            model.addAttribute("error", "文件格式错误");
        }
        
        model.addAttribute("title", "导入结果");
        
        return "import-user-result.page";
    }
    
    private String getStringCellValue(Cell cell) {
        if (cell != null) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        }
        return "";
    }
}
