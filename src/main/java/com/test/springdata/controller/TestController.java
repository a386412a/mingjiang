package com.test.springdata.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.springdata.entity.Persion;
import com.test.springdata.repsotory.PersionRepsotory;
import com.test.springdata.service.PersionService;

/**
 * @author tian
 这真的是个测试
 eclipse测试
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private PersionRepsotory persionRepsotory;
	@Autowired
	private PersionService persionService;

	@GetMapping("/hellow")
	public String test(String lastName) {
		Persion aa = persionRepsotory.getByLastName(lastName);
		return aa.toString();
	}

	@GetMapping("/like1")
	public String like1(String lastName, Integer id) {
		List<Persion> a = persionRepsotory
				.getByLastNameStartingWithAndIdLessThan(lastName, id);
		return a.toString();
	}

	@GetMapping("/like2")
	public String like2(String lastName, Integer id) {
		List<Persion> a = persionRepsotory.getByLastNameEndingWithAndId(
				lastName, id);
		return a.toString();
	}

	@GetMapping("/or")
	public String or() {
		List list = new ArrayList();
		list.add("aa@qq.com");
		list.add("bb@qq.com");
		list.add("cc@qq.com");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.parse("2020-09-15 11:39:05");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Persion> a = persionRepsotory.getByEmailInOrBrithLessThan(list,
				date);
		return a.toString();
	}

	// 自定义查询：查询id值最大的Persion
	@GetMapping("/getByMaxId")
	public String getByMaxId() {
		return persionRepsotory.getPersionByMaxId().toString();
	}

	// 自定义查询：占位符传参
	@GetMapping("/getbyIdAndName")
	public String getbyIdAndName(Integer id, String lastName) {
		return persionRepsotory.getByIdAndName(id, lastName).toString();
	}

	// 自定义查询：命名参数
	@GetMapping("/getByEmailAndName")
	public String getByEmailAndName(String email, String lastName) {
		return persionRepsotory.getByEmailAndName(lastName, email).toString();
	}

	// 自定义查询：占位符like查询
	@GetMapping("/likeByParam1")
	public String likeByParam1(String email, String lastName) {
		Persion psi = persionRepsotory.getLike1(email, lastName);
		if (psi == null) {
			return "未查询到数据";
		} else {
			return psi.toString();
		}
	}

	// 自定义查询：命名参数like查询
	@GetMapping("/likeByParam2")
	public String likeByParam2(String email, String lastName) {
		Persion psi = persionRepsotory.getLike2(email, lastName);
		if (psi == null) {
			return "未查询到数据";
		} else {
			return psi.toString();
		}
	}

	// 原生sql查询
	@GetMapping("/getBySql")
	public String getBySql() {
		return persionRepsotory.counts() + "";
	}

	// 修改数据
	@GetMapping("/update")
	public String updeta(String email, Integer id) {
		Integer count = persionService.update(email, id);
		return count + "";
	}
	
	// 新增数据
	@GetMapping("/save")
	public String save() {
		List<Persion> list = new ArrayList<>();
		for(int i = 'a'; i < 'z'; i++){
			Persion p = new Persion();
			p.setBrith(new Date());
			p.setEmail((char)i + "" + (char)i + "@qq.com");
			p.setLastName((char)i + "" + (char)i);
			list.add(p);
		}
		persionService.save(list);
		return "操作成功";
	}
	// 分页排序查询数据,虽然实现了分页,但是难以实现通用的待查询条件的分页
	@GetMapping("/search")
	public String search(){
		// 封装排序对象
		Order order = new Order(Direction.DESC,"id");
		Order order1 = new Order(Direction.ASC,"lastName");
		// 可变个数参数，即可以传递多个Order对象
		Sort sort = new Sort(order);
		// 实例化分页类
		int page = 1; // 分页下标从0开始,即1即是第二页
		int size = 6;
		// Pageables是一个接口，一般使用其实现类PageRequest来分页
		PageRequest pr = new PageRequest(page, size, sort);
		Page pages = persionRepsotory.findAll(pr);
		System.out.println("总条数：" + pages.getTotalElements());
		System.out.println("总页数：" + pages.getTotalPages());
		System.out.println("该页数据：" + pages.getContent());
		return pages.toString();
	}
	
	// 根据id批量查询
	@GetMapping("/searchById")
	public String searchById(){
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		List<Persion> lists = persionRepsotory.findAll(list);
		return lists.toString();
		
	}
	// 带参数通用分页查询
	@GetMapping("/pageByParam")
	public String pageByParam(){
		// JpaSpecificationExecutor的findAll(Specification<T> spec, Pageable pageable);方法
		// Specification为查询条件类
		// Pageable使用其实现类PageRequest
		int page = 0; // 分页下标从0开始,即1即是第二页
		int size = 6;
		// Pageables是一个接口，一般使用其实现类PageRequest来分页
		PageRequest pr = new PageRequest(page, size);
		Specification<Persion> sf = new Specification<Persion>() {

			// Root代表要查询的实体类
			// CriteriaQuery用来获取root对象,用于通知JPA要查询的实体类，并且可以添加查询条件
			// Predicate代表一个查询条件
			@Override
			public Predicate toPredicate(Root<Persion> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path path = root.get("id");
				// 各种条件方法需要仔细研究
				// 查询id大于5的数据
				Predicate predicate = cb.gt(path, 5);
				return predicate;
			}
		};
		Page<Persion> pages = persionRepsotory.findAll(sf, pr);
		System.out.println("总条数：" + pages.getTotalElements());
		System.out.println("总页数：" + pages.getTotalPages());
		System.out.println("该页数据：" + pages.getContent());
		return pages.toString();
	}
}
