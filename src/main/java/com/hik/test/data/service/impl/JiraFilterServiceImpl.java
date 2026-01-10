package com.hik.test.data.service.impl;

import com.hik.test.data.dto.JiraDetailDO;
import com.hik.test.data.manager.JiraTypeEnum;
import com.hik.test.data.mapper.JiraDetailMapper;
import com.hik.test.data.service.JiraFilterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraFilterServiceImpl implements JiraFilterService {

    private final JiraDetailMapper jiraDetailMapper;

}
