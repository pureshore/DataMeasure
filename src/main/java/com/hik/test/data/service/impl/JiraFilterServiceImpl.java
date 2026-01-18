package com.hik.test.data.service.impl;

import com.hik.test.data.mapper.JiraDetailMapper;
import com.hik.test.data.service.JiraFilterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraFilterServiceImpl implements JiraFilterService {

    private final JiraDetailMapper jiraDetailMapper;

}
